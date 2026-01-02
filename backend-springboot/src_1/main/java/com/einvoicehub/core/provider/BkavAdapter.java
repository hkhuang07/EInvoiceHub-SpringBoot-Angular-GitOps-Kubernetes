package com.einvoicehub.core.provider;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils; //Lỗi import org.springframework.util.Base64Utils
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Adapter cho BKAV eInvoice - Triển khai interface InvoiceProvider
 * BKAV sử dụng Web Service với mã hóa AES-256 và các CmdType khác nhau
 */
@Slf4j
@Service("BKAV")
public class BkavAdapter implements InvoiceProvider {

    private static final String PROVIDER_CODE = "BKAV";
    private static final String PROVIDER_NAME = "Bkav eInvoice";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${provider.bkav.timeout-ms:30000}")
    private int timeoutMs;

    @Value("${provider.bkav.base-url:https://einvoice.bkav.com/api/v1}")
    private String baseUrl;

    @Value("${provider.bkav.encryption-key:}")
    private String encryptionKey;

    public BkavAdapter(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.objectMapper = objectMapper;
    }

    @Override
    public String getProviderCode() {
        return PROVIDER_CODE;
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean isAvailable() {
        try {
            return testConnection(buildTestConfig());
        } catch (Exception e) {
            log.warn("BKAV Provider is not available: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config) {
        log.info("BKAV: Issuing invoice for request ID: {}", request.getClientRequestId());
        long startTime = System.currentTimeMillis();

        try {
            // Xây dựng payload theo format BKAV
            BkavCommandPayload payload = buildBkavPayload(request);

            // Gọi API với mã hóa
            String encryptedData = encryptPayload(payload);

            BkavApiResponse response = webClient
                    .post()
                    .uri("/exec-command")
                    .header("PartnerGUID", config.getUsername())
                    .header("PartnerToken", config.getPassword())
                    .bodyValue(Map.of("CommandData", encryptedData))
                    .retrieve()
                    .bodyToMono(BkavApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            long latency = System.currentTimeMillis() - startTime;
            log.info("BKAV: Invoice issued successfully. Latency: {}ms", latency);

            return convertToInvoiceResponse(response, InvoiceResponse.ResponseStatus.SUCCESS, latency);

        } catch (WebClientResponseException e) {
            long latency = System.currentTimeMillis() - startTime;
            log.error("BKAV: Failed to issue invoice. Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());

            BkavApiResponse errorResponse = parseErrorResponse(e.getResponseBodyAsString());
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    //Lỗi Incompatible types. Found: 'int', required: 'java.lang.String'
                    .errorCode(errorResponse != null ? errorResponse.getStatus() : "HTTP_" + e.getStatusCode().value())
                    .errorMessage(errorResponse != null ? extractErrorMessage(errorResponse) : e.getMessage())
                    .responseTime(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            long latency = System.currentTimeMillis() - startTime;
            log.error("BKAV: Error issuing invoice", e);

            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode("SYSTEM_ERROR")
                    .errorMessage(e.getMessage())
                    .responseTime(LocalDateTime.now())
                    .build();
        }
    }

    @Override
    public InvoiceStatus getInvoiceStatus(String invoiceNumber, ProviderConfig config) {
        try {
            BkavCommandPayload payload = BkavCommandPayload.builder()
                    .cmdType(850) // Lấy trạng thái hóa đơn
                    .commandObject(invoiceNumber)
                    .build();

            String encryptedData = encryptPayload(payload);

            Map<String, Object> request = Map.of("CommandData", encryptedData);

            BkavApiResponse response = webClient
                    .post()
                    .uri("/exec-command")
                    .header("PartnerGUID", config.getUsername())
                    .header("PartnerToken", config.getPassword())
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(BkavApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            if (response != null && response.getObject() != null) {
                return mapBkavStatus(response);
            }

        } catch (Exception e) {
            log.error("BKAV: Error getting invoice status", e);
        }

        return InvoiceStatus.FAILED;
    }

    @Override
    public InvoiceResponse cancelInvoice(String invoiceNumber, String reason, ProviderConfig config) {
        log.info("BKAV: Cancelling invoice: {}", invoiceNumber);

        try {
            BkavCommandPayload payload = BkavCommandPayload.builder()
                    .cmdType(201) // Hủy hóa đơn theo InvoiceGUID
                    .commandObject(Collections.singletonList(Map.of(
                            "InvoiceGUID", invoiceNumber,
                            "Reason", reason
                    )))
                    .build();

            String encryptedData = encryptPayload(payload);

            Map<String, Object> request = Map.of("CommandData", encryptedData);

            BkavApiResponse response = webClient
                    .post()
                    .uri("/exec-command")
                    .header("PartnerGUID", config.getUsername())
                    .header("PartnerToken", config.getPassword())
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(BkavApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            return convertToInvoiceResponse(response, InvoiceResponse.ResponseStatus.SUCCESS,
                    System.currentTimeMillis());

        } catch (Exception e) {
            log.error("BKAV: Error cancelling invoice", e);
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode("CANCEL_FAILED")
                    .errorMessage(e.getMessage())
                    .responseTime(LocalDateTime.now())
                    .build();
        }
    }

    @Override
    public InvoiceResponse replaceInvoice(String oldInvoiceNumber, InvoiceRequest newRequest,
                                          ProviderConfig config) {
        log.info("BKAV: Replacing invoice: {} -> new invoice", oldInvoiceNumber);

        try {
            // BKAV sử dụng CmdType 120 hoặc 123 cho thay thế hóa đơn
            BkavCommandPayload payload = buildBkavPayload(newRequest);
            payload.setCmdType(120); // Thay thế hóa đơn (PMKT cấp số)

            // Bổ sung thông tin hóa đơn gốc
            // Lỗi : Cannot resolve method 'isEmpty' in 'Object'
            if (payload.getCommandObject() != null && !payload.getCommandObject().isEmpty()) {
                Object commandObj = payload.getCommandObject().get(0);
                if (commandObj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> invoiceData = (Map<String, Object>) commandObj;
                    invoiceData.put("OriginalInvoiceIdentify",
                            String.format("[1]_[%s]_[%s]",
                                    invoiceData.getOrDefault("InvoiceSerial", ""),
                                    formatInvoiceNumberForBkav(oldInvoiceNumber)));
                }
            }

            String encryptedData = encryptPayload(payload);

            Map<String, Object> request = Map.of("CommandData", encryptedData);

            BkavApiResponse response = webClient
                    .post()
                    .uri("/exec-command")
                    .header("PartnerGUID", config.getUsername())
                    .header("PartnerToken", config.getPassword())
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(BkavApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            return convertToInvoiceResponse(response, InvoiceResponse.ResponseStatus.SUCCESS,
                    System.currentTimeMillis());

        } catch (Exception e) {
            log.error("BKAV: Error replacing invoice", e);
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode("REPLACE_FAILED")
                    .errorMessage(e.getMessage())
                    .responseTime(LocalDateTime.now())
                    .build();
        }
    }

    @Override
    public String getInvoicePdf(String invoiceNumber, ProviderConfig config) {
        try {
            BkavCommandPayload payload = BkavCommandPayload.builder()
                    .cmdType(816) // Lấy link tải hóa đơn bản thể hiện
                    .commandObject(Collections.singletonList(Map.of(
                            "PartnerInvoiceID", invoiceNumber,
                            "PartnerInvoiceStringID", ""
                    )))
                    .build();

            String encryptedData = encryptPayload(payload);

            Map<String, Object> request = Map.of("CommandData", encryptedData);

            BkavApiResponse response = webClient
                    .post()
                    .uri("/exec-command")
                    .header("PartnerGUID", config.getUsername())
                    .header("PartnerToken", config.getPassword())
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(BkavApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            if (response != null && response.getObject() != null) {
                String pdfUrl = extractPdfUrl(response);
                if (pdfUrl != null) {
                    return baseUrl + pdfUrl;
                }
            }

        } catch (Exception e) {
            log.error("BKAV: Error getting PDF", e);
        }

        return null;
    }

    @Override
    public String authenticate(ProviderConfig config) {
        // BKAV sử dụng PartnerGUID và PartnerToken trực tiếp trong header
        // Không cần token riêng như MISA
        return config.getUsername();
    }

    @Override
    public boolean testConnection(ProviderConfig config) {
        try {
            // Gọi API lấy thông tin để test kết nối
            BkavCommandPayload payload = BkavCommandPayload.builder()
                    .cmdType(904) // Tra cứu thông tin doanh nghiệp
                    .commandObject(config.getUsername()) // MST
                    .build();

            String encryptedData = encryptPayload(payload);
            Map<String, Object> request = Map.of("CommandData", encryptedData);

            BkavApiResponse response = webClient
                    .post()
                    .uri("/exec-command")
                    .header("PartnerGUID", config.getUsername())
                    .header("PartnerToken", config.getPassword())
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(BkavApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            return response != null && response.isOk();

        } catch (Exception e) {
            log.warn("BKAV: Connection test failed: {}", e.getMessage());
            return false;
        }
    }

    // ========== Private Helper Methods ==========

    private BkavCommandPayload buildBkavPayload(InvoiceRequest request) {
        // Xây dựng chi tiết hóa đơn
        List<Map<String, Object>> details = request.getItems().stream()
                .map(this::convertToBkavDetail)
                .collect(Collectors.toList());

        // Xây dựng thông tin hóa đơn
        Map<String, Object> invoiceData = new LinkedHashMap<>();
        invoiceData.put("InvoiceTypeID", getInvoiceTypeId(request));
        // Lỗi 'formatDateTime(java.time.LocalDateTime)' in 'com.einvoicehub.core.provider.BkavAdapter' cannot be applied to '(java.time.LocalDate)'
        invoiceData.put("InvoiceDate", formatDateTime(request.getIssueDate()));
        invoiceData.put("BuyerName", request.getBuyer() != null ? request.getBuyer().getName() : "");
        invoiceData.put("BuyerTaxCode", request.getBuyer() != null ?
                StringUtils.hasText(request.getBuyer().getTaxCode()) ?
                        request.getBuyer().getTaxCode() : "" : "");
        invoiceData.put("BuyerUnitName", request.getBuyer() != null ?
                StringUtils.hasText(request.getBuyer().getName()) ?
                        request.getBuyer().getName() : "" : "");
        invoiceData.put("BuyerAddress", request.getBuyer() != null ?
                request.getBuyer().getAddress() : "");
        invoiceData.put("BuyerBankAccount", request.getBuyer() != null ?
                request.getBuyer().getBankAccount() : "");
        invoiceData.put("PayMethodID", 3); // TM/CK
        invoiceData.put("ReceiveTypeID", 3); // Email & SMS
        invoiceData.put("ReceiverEmail", request.getBuyer() != null ?
                request.getBuyer().getEmail() : "");
        invoiceData.put("ReceiverMobile", request.getBuyer() != null ?
                request.getBuyer().getPhone() : "");
        invoiceData.put("Note", "");
        invoiceData.put("BillCode", request.getClientRequestId() != null ?
                request.getClientRequestId() : "");
        invoiceData.put("CurrencyID", request.getSummary() != null &&
                StringUtils.hasText(request.getSummary().getCurrencyCode()) ?
                request.getSummary().getCurrencyCode() : "VND");
        invoiceData.put("ExchangeRate", 1.0);

        // Lấy mẫu số và ký hiệu từ extra config
        if (request.getExtraConfig() != null) {
            if (request.getExtraConfig().containsKey("InvoiceForm")) {
                invoiceData.put("InvoiceForm", request.getExtraConfig().get("InvoiceForm"));
            }
            if (request.getExtraConfig().containsKey("InvoiceSerial")) {
                invoiceData.put("InvoiceSerial", request.getExtraConfig().get("InvoiceSerial"));
            }
        }

        // Số hóa đơn = 0 để BKAV cấp tự động
        invoiceData.put("InvoiceNo", 0);

        // Xây dựng command object theo format BKAV
        List<Map<String, Object>> commandObject = new ArrayList<>();
        Map<String, Object> invoiceWrapper = new LinkedHashMap<>();
        invoiceWrapper.put("Invoice", invoiceData);
        invoiceWrapper.put("ListInvoiceDetailsWS", details);
        invoiceWrapper.put("ListInvoiceAttachFileWS", Collections.emptyList());
        invoiceWrapper.put("PartnerInvoiceID", request.getInvoiceMetadataId() != null ?
                request.getInvoiceMetadataId() : System.currentTimeMillis());
        invoiceWrapper.put("PartnerInvoiceStringID", "");
        commandObject.add(invoiceWrapper);

        // CmdType 111: PMKT cấp mẫu số, ký hiệu, số hóa đơn
        int cmdType = 111;
        if (request.getExtraConfig() != null && request.getExtraConfig().containsKey("CmdType")) {
            cmdType = (Integer) request.getExtraConfig().get("CmdType");
        }

        return BkavCommandPayload.builder()
                .cmdType(cmdType)
                .commandObject(commandObject)
                .build();
    }

    private int getInvoiceTypeId(InvoiceRequest request) {
        if (request.getExtraConfig() != null && request.getExtraConfig().containsKey("InvoiceTypeID")) {
            return (Integer) request.getExtraConfig().get("InvoiceTypeID");
        }
        return 1; // Mặc định là Hóa đơn GTGT
    }

    private Map<String, Object> convertToBkavDetail(InvoiceRequest.InvoiceItem item) {
        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("ItemName", item.getItemName());
        detail.put("UnitName", item.getUnitName() != null ? item.getUnitName() : "");
        detail.put("Qty", item.getQuantity());
        detail.put("Price", item.getUnitPrice());
        detail.put("Amount", item.getAmount());
        detail.put("TaxRateID", mapTaxRateToBkavId(item.getTaxRate()));
        detail.put("TaxRate", item.getTaxRate());
        // Lỗi :         detail.put("TaxRateID", mapTaxRateToBkavId(item.getTaxRate()));
        detail.put("TaxAmount", item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO);
        detail.put("DiscountRate", 0.0);
        detail.put("DiscountAmount", "");
        detail.put("IsDiscount", false);
        detail.put("UserDefineDetails", "");
        detail.put("ItemTypeID", 0); // Hàng hóa dịch vụ thường
        return detail;
    }

    private int mapTaxRateToBkavId(BigDecimal taxRate) {
        if (taxRate == null) return 1;
        double rate = taxRate.doubleValue();
        if (rate == 0) return 1;
        if (rate == 5) return 2;
        if (rate == 10) return 3;
        if (rate == -1) return 4; // Không chịu thuế
        return 3; // Mặc định 10%
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    }

    private String formatInvoiceNumberForBkav(String invoiceNumber) {
        // Format số hóa đơn theo định dạng BKAV (8 chữ số với số 0 đầu)
        try {
            int num = Integer.parseInt(invoiceNumber);
            return String.format("%08d", num);
        } catch (NumberFormatException e) {
            return String.format("%08d", 1);
        }
    }

    private String encryptPayload(BkavCommandPayload payload) {
        try {
            // Chuyển đổi payload sang JSON
            String jsonData = objectMapper.writeValueAsString(payload);

            // Nếu không có encryption key, trả về JSON gốc
            if (!StringUtils.hasText(encryptionKey)) {
                return jsonData;
            }

            // Mã hóa AES-256
            SecretKeySpec secretKey = new SecretKeySpec(
                    encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");

            byte[] iv = new byte[12];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] encrypted = cipher.doFinal(jsonData.getBytes(StandardCharsets.UTF_8));

            // Encode IV + encrypted data sang Base64
            // Lỗi Cannot resolve symbol 'Base64Utils'
            String ivBase64 = Base64Utils.encodeToString(iv);
            String encryptedBase64 = Base64Utils.encodeToString(encrypted);

            return ivBase64 + ":" + encryptedBase64;

        } catch (Exception e) {
            log.error("BKAV: Error encrypting payload", e);
            throw new RuntimeException("Failed to encrypt payload", e);
        }
    }

    private InvoiceResponse convertToInvoiceResponse(BkavApiResponse bkavResponse,
                                                     InvoiceResponse.ResponseStatus status,
                                                     long latencyMs) {
        if (bkavResponse == null) {
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode("NULL_RESPONSE")
                    .errorMessage("No response from BKAV")
                    .responseTime(LocalDateTime.now())
                    .build();
        }

        boolean isSuccess = bkavResponse.isOk() && !bkavResponse.isError();

        return InvoiceResponse.builder()
                .status(isSuccess ? InvoiceResponse.ResponseStatus.SUCCESS :
                        InvoiceResponse.ResponseStatus.FAILED)
                //Lỗi 'errorCode(java.lang.String)' in 'com.einvoicehub.core.provider.InvoiceResponse.InvoiceResponseBuilder' cannot be applied to '(java.lang.Integer)'
                .errorCode(isSuccess ? null : bkavResponse.getStatus())
                .errorMessage(isSuccess ? null : extractErrorMessage(bkavResponse))
                .transactionCode(extractTransactionCode(bkavResponse))
                .invoiceNumber(extractInvoiceNumber(bkavResponse))
                .symbolCode(extractSymbolCode(bkavResponse))
                .lookupCode(extractLookupCode(bkavResponse))
                .pdfUrl(extractPdfUrl(bkavResponse))
                .responseTime(LocalDateTime.now())
                .rawResponse(bkavResponse)
                .build();
    }

    private InvoiceStatus mapBkavStatus(BkavApiResponse response) {
        try {
            if (response.getObject() != null) {
                JsonNode objectNode = objectMapper.readTree(response.getObject().toString());
                if (objectNode.isArray() && !objectNode.isEmpty()) {
                    JsonNode firstItem = objectNode.get(0);
                    int bkavStatus = firstItem.path("BkavStatus").asInt();
                    int taxStatus = firstItem.path("TaxStatus").asInt();

                    return mapBkavStatusToInvoiceStatus(bkavStatus, taxStatus);
                }
            }
        } catch (Exception e) {
            log.error("Error parsing BKAV status response", e);
        }
        return InvoiceStatus.FAILED;
    }

    private InvoiceStatus mapBkavStatusToInvoiceStatus(int bkavStatus, int taxStatus) {
        // Mapping từ InvoiceStatusID của BKAV
        switch (bkavStatus) {
            case 1: // Mới tạo
            case 11: // Trống (Đã cấp số, chờ ký)
                return InvoiceStatus.SIGNING;
            case 2: // Đã phát hành
                if (taxStatus == 33) {
                    return InvoiceStatus.SUCCESS; // Thuế đã duyệt
                }
                return InvoiceStatus.SENT_TO_PROVIDER;
            case 3: // Đã hủy
                return InvoiceStatus.CANCELLED;
            case 6: // Thay thế
                return InvoiceStatus.REPLACED;
            case 8: // Điều chỉnh
                return InvoiceStatus.SUCCESS;
            default:
                return InvoiceStatus.FAILED;
        }
    }

    private String extractTransactionCode(BkavApiResponse response) {
        try {
            if (response.getObject() != null) {
                JsonNode objectNode = objectMapper.readTree(response.getObject().toString());
                if (objectNode.isArray() && !objectNode.isEmpty()) {
                    return objectNode.get(0).path("InvoiceGUID").asText();
                }
            }
        } catch (Exception e) {
            log.debug("Error extracting transaction code", e);
        }
        return null;
    }

    private String extractInvoiceNumber(BkavApiResponse response) {
        try {
            if (response.getObject() != null) {
                JsonNode objectNode = objectMapper.readTree(response.getObject().toString());
                if (objectNode.isArray() && !objectNode.isEmpty()) {
                    return objectNode.get(0).path("InvoiceNo").asText();
                }
            }
        } catch (Exception e) {
            log.debug("Error extracting invoice number", e);
        }
        return null;
    }

    private String extractSymbolCode(BkavApiResponse response) {
        try {
            if (response.getObject() != null) {
                JsonNode objectNode = objectMapper.readTree(response.getObject().toString());
                if (objectNode.isArray() && !objectNode.isEmpty()) {
                    return objectNode.get(0).path("InvoiceSerial").asText();
                }
            }
        } catch (Exception e) {
            log.debug("Error extracting symbol code", e);
        }
        return null;
    }

    private String extractLookupCode(BkavApiResponse response) {
        try {
            if (response.getObject() != null) {
                JsonNode objectNode = objectMapper.readTree(response.getObject().toString());
                if (objectNode.isArray() && !objectNode.isEmpty()) {
                    return objectNode.get(0).path("MTC").asText();
                }
            }
        } catch (Exception e) {
            log.debug("Error extracting lookup code", e);
        }
        return null;
    }

    private String extractPdfUrl(BkavApiResponse response) {
        try {
            if (response.getObject() != null) {
                JsonNode objectNode = objectMapper.readTree(response.getObject().toString());
                if (objectNode.isArray() && !objectNode.isEmpty()) {
                    return objectNode.get(0).path("MessLog").asText();
                }
            }
        } catch (Exception e) {
            log.debug("Error extracting PDF URL", e);
        }
        return null;
    }

    private String extractErrorMessage(BkavApiResponse response) {
        try {
            if (response.getObject() != null) {
                JsonNode objectNode = objectMapper.readTree(response.getObject().toString());
                if (objectNode.isArray() && !objectNode.isEmpty()) {
                    return objectNode.get(0).path("MessLog").asText();
                }
            }
            //Lỗi                     return objectNode.get(0).path("MessLog").asText();
            return response.getStatus();
        } catch (Exception e) {
            return response.getStatus();
        }
    }

    private BkavApiResponse parseErrorResponse(String body) {
        try {
            return objectMapper.readValue(body, BkavApiResponse.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private ProviderConfig buildTestConfig() {
        return ProviderConfig.builder()
                .providerCode(PROVIDER_CODE)
                .username("test-guid")
                .password("test-token")
                .build();
    }

    // ========== Inner Classes for BKAV API ==========

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class BkavCommandPayload {
        private int cmdType;
        private Object commandObject;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class BkavApiResponse {
        private int status;
        private Object object;
        private boolean isOk;
        private boolean isError;
        private String code;
    }
}