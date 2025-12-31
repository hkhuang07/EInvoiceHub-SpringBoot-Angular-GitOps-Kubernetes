package com.einvoicehub.core.provider;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Adapter cho MISA MeInvoice - Triển khai interface InvoiceProvider
 * MISA sử dụng REST API với Bearer Token authentication
 */
@Slf4j
@Service("MISA")
public class MisaAdapter implements InvoiceProvider {

    private static final String PROVIDER_CODE = "MISA";
    private static final String PROVIDER_NAME = "MISA MeInvoice";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${provider.misa.timeout-ms:30000}")
    private int timeoutMs;

    @Value("${provider.misa.base-url:https://api.meinvoice.vn/api/integration}")
    private String baseUrl;

    @Value("${provider.misa.test-base-url:https://testapi.meinvoice.vn/api/integration}")
    private String testBaseUrl;

    private String cachedToken;
    private LocalDateTime tokenExpiry;

    public MisaAdapter(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .baseUrl(testBaseUrl) // Mặc định là test
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
            log.warn("MISA Provider is not available: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config) {
        log.info("MISA: Issuing invoice for request ID: {}", request.getClientRequestId());
        long startTime = System.currentTimeMillis();

        try {
            // Lấy token xác thực
            String token = getOrRefreshToken(config);

            // Xây dựng payload theo format MISA
            MisaInvoicePayload payload = buildMisaPayload(request);

            // Gọi API phát hành hóa đơn với SignType = 2 (HSM, có hiển thị CKS)
            int signType = 2;
            if (config.getExtraConfigJson() != null) {
                try {
                    JsonNode configNode = objectMapper.readTree(config.getExtraConfigJson());
                    signType = configNode.path("SignType").asInt(2);
                } catch (Exception e) {
                    log.debug("Error parsing MISA config, using default SignType");
                }
            }

            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("SignType", signType);
            requestBody.put("InvoiceData", Collections.singletonList(payload));
            requestBody.put("PublishInvoiceData", null);

            MisaApiResponse response = webClient
                    .post()
                    .uri("/invoice")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(MisaApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            long latency = System.currentTimeMillis() - startTime;
            log.info("MISA: Invoice issued successfully. Latency: {}ms", latency);

            return convertToInvoiceResponse(response, InvoiceResponse.ResponseStatus.SUCCESS, latency);

        } catch (WebClientResponseException e) {
            long latency = System.currentTimeMillis() - startTime;
            log.error("MISA: Failed to issue invoice. Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());

            MisaApiResponse errorResponse = parseErrorResponse(e.getResponseBodyAsString());
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode(errorResponse != null ? errorResponse.getErrorCode() : "HTTP_" + e.getStatusCode().value())
                    .errorMessage(errorResponse != null ? errorResponse.getDescriptionErrorCode() : e.getMessage())
                    .responseTime(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            long latency = System.currentTimeMillis() - startTime;
            log.error("MISA: Error issuing invoice", e);

            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode("SYSTEM_ERROR")
                    .errorMessage(e.getMessage())
                    .responseTime(LocalDateTime.now())
                    .build();
        }
    }

    @Override
    public InvoiceStatus getInvoiceStatus(String transactionId, ProviderConfig config) {
        try {
            String token = getOrRefreshToken(config);

            Map<String, Object> params = new LinkedHashMap<>();
            params.put("invoiceWithCode", true);
            params.put("invoiceCalcu", false);
            params.put("inputType", 1); // Lấy theo TransactionID

            Map<String, Object> requestBody = Collections.singletonList(transactionId);

            MisaApiResponse response = webClient
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/invoice/status")
                            .queryParams(toQueryParams(params))
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(MisaApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            if (response != null && response.getData() != null) {
                return mapMisaStatus(response);
            }

        } catch (Exception e) {
            log.error("MISA: Error getting invoice status", e);
        }

        return InvoiceStatus.FAILED;
    }

    @Override
    public InvoiceResponse cancelInvoice(String transactionId, String reason, ProviderConfig config) {
        log.info("MISA: Cancelling invoice: {}", transactionId);

        try {
            // MISA không có API hủy trực tiếp, cần liên hệ hỗ trợ hoặc thay thế
            log.warn("MISA: Direct cancellation not supported. Invoice: {}", transactionId);

            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode("CANCEL_NOT_SUPPORTED")
                    .errorMessage("MISA does not support direct cancellation. Please contact MISA support or use replacement invoice.")
                    .responseTime(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("MISA: Error cancelling invoice", e);
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode("CANCEL_FAILED")
                    .errorMessage(e.getMessage())
                    .responseTime(LocalDateTime.now())
                    .build();
        }
    }

    @Override
    public InvoiceResponse replaceInvoice(String oldTransactionId, InvoiceRequest newRequest,
                                          ProviderConfig config) {
        log.info("MISA: Replacing invoice: {} -> new invoice", oldTransactionId);

        try {
            // Lấy thông tin hóa đơn cũ để lấy thông tin thay thế
            String token = getOrRefreshToken(config);

            // Bổ sung thông tin hóa đơn gốc vào request
            MisaInvoicePayload payload = buildMisaPayload(newRequest);
            payload.setReferenceType(1); // 1: Thay thế
            payload.setOrgInvNo(oldTransactionId); // TransactionID của hóa đơn cũ
            payload.setInvoiceNote("Thay thế hóa đơn: " + reason);

            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("SignType", 2);
            requestBody.put("InvoiceData", Collections.singletonList(payload));
            requestBody.put("PublishInvoiceData", null);

            MisaApiResponse response = webClient
                    .post()
                    .uri("/invoice")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(MisaApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            return convertToInvoiceResponse(response, InvoiceResponse.ResponseStatus.SUCCESS,
                    System.currentTimeMillis());

        } catch (Exception e) {
            log.error("MISA: Error replacing invoice", e);
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode("REPLACE_FAILED")
                    .errorMessage(e.getMessage())
                    .responseTime(LocalDateTime.now())
                    .build();
        }
    }

    @Override
    public String getInvoicePdf(String transactionId, ProviderConfig config) {
        try {
            String token = getOrRefreshToken(config);

            Map<String, Object> params = new LinkedHashMap<>();
            params.put("invoiceWithCode", true);
            params.put("invoiceCalcu", false);
            params.put("downloadDataType", "pdf");

            Map<String, Object> requestBody = Collections.singletonList(transactionId);

            MisaApiResponse response = webClient
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/invoice/download")
                            .queryParams(toQueryParams(params))
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(MisaApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            if (response != null && response.getData() != null) {
                return extractPdfUrl(response);
            }

        } catch (Exception e) {
            log.error("MISA: Error getting PDF", e);
        }

        return null;
    }

    @Override
    public String authenticate(ProviderConfig config) {
        try {
            Map<String, Object> authRequest = new LinkedHashMap<>();
            authRequest.put("appid", config.getUsername());
            authRequest.put("taxcode", config.getUsername()); // AppID same as taxcode
            authRequest.put("username", config.getUsername());
            authRequest.put("password", config.getPassword());

            Map<String, Object> response = webClient
                    .post()
                    .uri("/auth/token")
                    .bodyValue(authRequest)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            if (response != null && response.containsKey("Data")) {
                return response.get("Data").toString();
            }

        } catch (Exception e) {
            log.error("MISA: Authentication failed", e);
        }

        return null;
    }

    @Override
    public boolean testConnection(ProviderConfig config) {
        try {
            String token = getOrRefreshToken(config);
            return token != null && !token.isEmpty();
        } catch (Exception e) {
            log.warn("MISA: Connection test failed: {}", e.getMessage());
            return false;
        }
    }

    // ========== Private Helper Methods ==========

    private String getOrRefreshToken(ProviderConfig config) {
        if (cachedToken != null && tokenExpiry != null &&
                tokenExpiry.isAfter(LocalDateTime.now().plusMinutes(5))) {
            return cachedToken;
        }

        try {
            Map<String, Object> authRequest = new LinkedHashMap<>();
            authRequest.put("appid", config.getUsername());
            authRequest.put("taxcode", config.getUsername());
            authRequest.put("username", config.getUsername());
            authRequest.put("password", config.getPassword());

            Map<String, Object> response = webClient
                    .post()
                    .uri("/auth/token")
                    .bodyValue(authRequest)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            if (response != null && Boolean.TRUE.equals(response.get("Success"))) {
                cachedToken = response.get("Data").toString();
                tokenExpiry = LocalDateTime.now().plusDays(7); // Token có hạn 14 ngày
                return cachedToken;
            }

        } catch (Exception e) {
            log.error("MISA: Failed to get token", e);
        }

        throw new RuntimeException("Failed to authenticate with MISA");
    }

    private MisaInvoicePayload buildMisaPayload(InvoiceRequest request) {
        // Xây dựng chi tiết hóa đơn
        List<MisaInvoiceDetail> details = request.getItems().stream()
                .map(this::convertToMisaDetail)
                .collect(Collectors.toList());

        // Xây dựng tổng hợp thuế suất
        List<MisaTaxRateInfo> taxRateInfo = buildTaxRateInfo(details);

        // Xây dựng payload
        MisaInvoicePayload payload = MisaInvoicePayload.builder()
                .refId(UUID.randomUUID().toString())
                .invSeries(getInvSeries(request))
                .invDate(request.getIssueDate() != null ?
                        request.getIssueDate().format(DateTimeFormatter.ISO_DATE) :
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))
                .currencyCode(request.getSummary() != null &&
                        StringUtils.hasText(request.getSummary().getCurrencyCode()) ?
                        request.getSummary().getCurrencyCode() : "VND")
                .exchangeRate(1.0)
                .paymentMethodName("TM/CK")
                .isInvoiceSummary(false)
                .buyerLegalName(request.getBuyer() != null ?
                        StringUtils.hasText(request.getBuyer().getName()) ?
                                request.getBuyer().getName() : "" : "")
                .buyerTaxCode(request.getBuyer() != null ?
                        StringUtils.hasText(request.getBuyer().getTaxCode()) ?
                                request.getBuyer().getTaxCode() : "" : "")
                .buyerAddress(request.getBuyer() != null ?
                        request.getBuyer().getAddress() : "")
                .buyerFullName(request.getBuyer() != null ?
                        request.getBuyer().getName() : "")
                .buyerPhoneNumber(request.getBuyer() != null ?
                        request.getBuyer().getPhone() : "")
                .buyerEmail(request.getBuyer() != null ?
                        request.getBuyer().getEmail() : "")
                .buyerBankAccount(request.getBuyer() != null ?
                        request.getBuyer().getBankAccount() : "")
                .buyerBankName(request.getBuyer() != null ?
                        request.getBuyer().getBankName() : "")
                .originalInvoiceDetail(details)
                .taxRateInfo(taxRateInfo)
                .build();

        // Tính toán các trường tổng tiền
        calculateTotals(payload, request);

        return payload;
    }

    private void calculateTotals(MisaInvoicePayload payload, InvoiceRequest request) {
        BigDecimal totalSaleAmountOC = BigDecimal.ZERO;
        BigDecimal totalDiscountAmountOC = BigDecimal.ZERO;
        BigDecimal totalVATAmountOC = BigDecimal.ZERO;

        for (MisaInvoiceDetail detail : payload.getOriginalInvoiceDetail()) {
            BigDecimal amountOC = detail.getAmountOC() != null ? detail.getAmountOC() : BigDecimal.ZERO;
            BigDecimal discountAmountOC = detail.getDiscountAmountOC() != null ? detail.getDiscountAmountOC() : BigDecimal.ZERO;
            BigDecimal vatAmountOC = detail.getVATAmountOC() != null ? detail.getVATAmountOC() : BigDecimal.ZERO;

            totalSaleAmountOC = totalSaleAmountOC.add(amountOC);
            totalDiscountAmountOC = totalDiscountAmountOC.add(discountAmountOC);
            totalVATAmountOC = totalVATAmountOC.add(vatAmountOC);
        }

        BigDecimal totalAmountWithoutVATOC = totalSaleAmountOC.subtract(totalDiscountAmountOC);
        BigDecimal totalAmountOC = totalAmountWithoutVATOC.add(totalVATAmountOC);

        payload.setTotalSaleAmountOC(totalSaleAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalSaleAmount(totalSaleAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalDiscountAmountOC(totalDiscountAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalDiscountAmount(totalDiscountAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalAmountWithoutVATOC(totalAmountWithoutVATOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalAmountWithoutVAT(totalAmountWithoutVATOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalVATAmountOC(totalVATAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalVATAmount(totalVATAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalAmountOC(totalAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalAmount(totalAmountOC.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalAmountInWords(convertNumberToWords(totalAmountOC));
    }

    private String getInvSeries(InvoiceRequest request) {
        if (request.getExtraConfig() != null && request.getExtraConfig().containsKey("InvSeries")) {
            return (String) request.getExtraConfig().get("InvSeries");
        }
        // Mặc định - sẽ được MISA cấp
        return "";
    }

    private MisaInvoiceDetail convertToMisaDetail(InvoiceRequest.InvoiceItem item) {
        BigDecimal quantity = item.getQuantity() != null ? item.getQuantity() : BigDecimal.ONE;
        BigDecimal unitPrice = item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO;
        BigDecimal amountOC = quantity.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);

        BigDecimal discountRate = item.getDiscountAmount() != null && amountOC.compareTo(BigDecimal.ZERO) > 0
                ? item.getDiscountAmount().multiply(BigDecimal.valueOf(100)).divide(amountOC, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal discountAmountOC = amountOC.multiply(discountRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal amountWithoutVATOC = amountOC.subtract(discountAmountOC);

        BigDecimal taxRate = item.getTaxRate() != null ? item.getTaxRate() : BigDecimal.ZERO;
        BigDecimal vatAmountOC = amountWithoutVATOC.multiply(taxRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return MisaInvoiceDetail.builder()
                .itemType(1) // Hàng hóa thường
                .lineNumber(1)
                .sortOrder(1)
                .itemCode(item.getItemCode() != null ? item.getItemCode() : "")
                .itemName(item.getItemName())
                .unitName(item.getUnitName() != null ? item.getUnitName() : "")
                .quantity(quantity.setScale(2, RoundingMode.HALF_UP))
                .unitPrice(unitPrice.setScale(2, RoundingMode.HALF_UP))
                .amountOC(amountOC)
                .amount(amountOC)
                .discountRate(discountRate.setScale(2, RoundingMode.HALF_UP))
                .discountAmountOC(discountAmountOC)
                .discountAmount(discountAmountOC)
                .amountWithoutVATOC(amountWithoutVATOC)
                .amountWithoutVAT(amountWithoutVATOC)
                .vatRateName(mapTaxRateToMisaName(taxRate))
                .vatAmountOC(vatAmountOC)
                .vatAmount(vatAmountOC)
                .build();
    }

    private String mapTaxRateToMisaName(BigDecimal taxRate) {
        if (taxRate == null) return "0%";
        double rate = taxRate.doubleValue();
        if (rate == 0) return "0%";
        if (rate == 5) return "5%";
        if (rate == 8) return "8%";
        if (rate == 10) return "10%";
        return "0%";
    }

    private List<MisaTaxRateInfo> buildTaxRateInfo(List<MisaInvoiceDetail> details) {
        Map<String, MisaTaxRateInfo> taxMap = new LinkedHashMap<>();

        for (MisaInvoiceDetail detail : details) {
            String vatRateName = detail.getVatRateName();
            if (!taxMap.containsKey(vatRateName)) {
                taxMap.put(vatRateName, MisaTaxRateInfo.builder()
                        .vatRateName(vatRateName)
                        .amountWithoutVATOC(BigDecimal.ZERO)
                        .vatAmountOC(BigDecimal.ZERO)
                        .build());
            }

            MisaTaxRateInfo info = taxMap.get(vatRateName);
            BigDecimal amountOC = detail.getAmountWithoutVATOC() != null ?
                    detail.getAmountWithoutVATOC() : BigDecimal.ZERO;
            BigDecimal vatAmountOC = detail.getVatAmountOC() != null ?
                    detail.getVatAmountOC() : BigDecimal.ZERO;

            info.setAmountWithoutVATOC(info.getAmountWithoutVATOC().add(amountOC));
            info.setVatAmountOC(info.getVatAmountOC().add(vatAmountOC));
        }

        return new ArrayList<>(taxMap.values());
    }

    private String convertNumberToWords(BigDecimal amount) {
        // Simplified number to Vietnamese words conversion
        // In production, use a proper library like vietnammoneytext
        try {
            long wholePart = amount.longValue();
            return convertWholeNumberToWords(wholePart) + " đồng.";
        } catch (Exception e) {
            return "";
        }
    }

    private String convertWholeNumberToWords(long number) {
        if (number == 0) return "Không";

        String[] units = {"", "nghìn", "triệu", "tỷ"};
        String[] digits = {"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};

        String result = "";
        int unitIndex = 0;

        while (number > 0) {
            long group = number % 1000;
            if (group > 0) {
                String groupText = convertGroupToWords((int) group, digits);
                result = groupText + " " + units[unitIndex] + " " + result;
            }
            number /= 1000;
            unitIndex++;
        }

        return result.trim();
    }

    private String convertGroupToWords(int number, String[] digits) {
        String[] tens = {"", "mười", "hai mươi", "ba mươi", "bốn mươi", "năm mươi", "sáu mươi", "bảy mươi", "tám mươi", "chín mươi"};

        String result = "";
        int hundreds = number / 100;
        int remainder = number % 100;

        if (hundreds > 0) {
            result = digits[hundreds] + " trăm ";
        }

        if (remainder > 0) {
            if (remainder < 10) {
                result += digits[remainder];
            } else if (remainder < 20) {
                result += "mười " + digits[remainder - 10];
            } else {
                int tensDigit = remainder / 10;
                int onesDigit = remainder % 10;
                result += tens[tensDigit];
                if (onesDigit > 0) {
                    result += " " + digits[onesDigit];
                }
            }
        }

        return result.trim();
    }

    private InvoiceResponse convertToInvoiceResponse(MisaApiResponse misaResponse,
                                                     InvoiceResponse.ResponseStatus status,
                                                     long latencyMs) {
        if (misaResponse == null) {
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode("NULL_RESPONSE")
                    .errorMessage("No response from MISA")
                    .responseTime(LocalDateTime.now())
                    .build();
        }

        boolean isSuccess = Boolean.TRUE.equals(misaResponse.getSuccess()) &&
                (misaResponse.getPublishInvoiceResult() == null ||
                        !misaResponse.getPublishInvoiceResult().isEmpty());

        if (isSuccess && misaResponse.getPublishInvoiceResult() != null &&
                !misaResponse.getPublishInvoiceResult().isEmpty()) {
            Map<String, Object> result = misaResponse.getPublishInvoiceResult().get(0);

            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.SUCCESS)
                    .errorCode(null)
                    .errorMessage(null)
                    .transactionCode((String) result.getOrDefault("TransactionID", ""))
                    .invoiceNumber((String) result.getOrDefault("InvNo", ""))
                    .symbolCode((String) result.getOrDefault("InvSeries", ""))
                    .lookupCode((String) result.getOrDefault("TransactionID", ""))
                    .responseTime(LocalDateTime.now())
                    .rawResponse(misaResponse)
                    .build();
        }

        return InvoiceResponse.builder()
                .status(InvoiceResponse.ResponseStatus.FAILED)
                .errorCode(misaResponse.getErrorCode())
                .errorMessage(misaResponse.getDescriptionErrorCode())
                .responseTime(LocalDateTime.now())
                .rawResponse(misaResponse)
                .build();
    }

    private InvoiceStatus mapMisaStatus(MisaApiResponse response) {
        try {
            if (response.getData() != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.getData();
                if (!dataList.isEmpty()) {
                    Map<String, Object> invoiceStatus = dataList.get(0);
                    int eInvoiceStatus = ((Number) invoiceStatus.getOrDefault("EInvoiceStatus", 0)).intValue();

                    return mapMisaStatusToInvoiceStatus(eInvoiceStatus);
                }
            }
        } catch (Exception e) {
            log.error("Error parsing MISA status response", e);
        }
        return InvoiceStatus.FAILED;
    }

    private InvoiceStatus mapMisaStatusToInvoiceStatus(int eInvoiceStatus) {
        // Mapping từ EInvoiceStatus của MISA
        // 1: hóa đơn gốc
        // 2: hóa đơn bị hủy
        // 3: hóa đơn thay thế
        // 5: hóa đơn điều chỉnh
        // 7: hóa đơn bị thay thế
        // 8: hóa đơn bị điều chỉnh

        switch (eInvoiceStatus) {
            case 1:
                return InvoiceStatus.SUCCESS;
            case 2:
                return InvoiceStatus.CANCELLED;
            case 3:
            case 7:
                return InvoiceStatus.REPLACED;
            case 5:
            case 8:
                return InvoiceStatus.SUCCESS;
            default:
                return InvoiceStatus.FAILED;
        }
    }

    private String extractPdfUrl(MisaApiResponse response) {
        try {
            if (response.getData() != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.getData();
                if (!dataList.isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> invoiceData = (Map<String, Object>) dataList.get(0).get("InvoiceDataPublished");
                    if (invoiceData != null && invoiceData.containsKey("Data")) {
                        // Trả về dữ liệu base64 - cần decode và lưu file
                        return (String) invoiceData.get("Data");
                    }
                }
            }
        } catch (Exception e) {
            log.debug("Error extracting PDF URL", e);
        }
        return null;
    }

    private MisaApiResponse parseErrorResponse(String body) {
        try {
            return objectMapper.readValue(body, MisaApiResponse.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private Map<String, String> toQueryParams(Map<String, Object> params) {
        Map<String, String> queryParams = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            queryParams.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return queryParams;
    }

    private ProviderConfig buildTestConfig() {
        return ProviderConfig.builder()
                .providerCode(PROVIDER_CODE)
                .username("test-app-id")
                .password("test-password")
                .build();
    }

    // ========== Inner Classes for MISA API ==========

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class MisaInvoicePayload {
        private String refId;
        private String invSeries;
        private String invDate;
        private String currencyCode;
        private BigDecimal exchangeRate;
        private String paymentMethodName;
        private Boolean isInvoiceSummary;
        private Boolean isSendEmail;
        private String receiverName;
        private String receiverEmail;
        private String sellerShopCode;
        private String sellerShopName;
        private String buyerLegalName;
        private String buyerTaxCode;
        private String buyerAddress;
        private String buyerFullName;
        private String buyerPhoneNumber;
        private String buyerEmail;
        private String buyerBankAccount;
        private String buyerBankName;
        private String buyerIDNumber;
        private String buyerPassport;
        private String buyerBudgetCode;
        private BigDecimal totalSaleAmountOC;
        private BigDecimal totalSaleAmount;
        private BigDecimal totalDiscountAmountOC;
        private BigDecimal totalDiscountAmount;
        private BigDecimal totalAmountWithoutVATOC;
        private BigDecimal totalAmountWithoutVAT;
        private BigDecimal totalVATAmountOC;
        private BigDecimal totalVATAmount;
        private BigDecimal totalAmountOC;
        private BigDecimal totalAmount;
        private String totalAmountInWords;
        // Thông tin điều chỉnh/thay thế
        private Integer referenceType; // 1: Thay thế, 2: Điều chỉnh
        private Integer orgInvoiceType;
        private String orgInvTemplateNo;
        private String orgInvSeries;
        private String orgInvNo;
        private String orgInvDate;
        private String invoiceNote;
        // Chi tiết
        private List<MisaInvoiceDetail> originalInvoiceDetail;
        private List<MisaTaxRateInfo> taxRateInfo;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class MisaInvoiceDetail {
        private Integer itemType;
        private Integer sortOrder;
        private Integer lineNumber;
        private String itemCode;
        private String itemName;
        private String unitName;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal amountOC;
        private BigDecimal amount;
        private BigDecimal discountRate;
        private BigDecimal discountAmountOC;
        private BigDecimal discountAmount;
        private BigDecimal amountWithoutVATOC;
        private BigDecimal amountWithoutVAT;
        private String vatRateName;
        private BigDecimal vatAmountOC;
        private BigDecimal vatAmount;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class MisaTaxRateInfo {
        private String vatRateName;
        private BigDecimal amountWithoutVATOC;
        private BigDecimal vatAmountOC;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class MisaApiResponse {
        private Boolean success;
        private String errorCode;
        private String descriptionErrorCode;
        private List<Map<String, Object>> createInvoiceResult;
        private List<Map<String, Object>> publishInvoiceResult;
        private List<String> errors;
        private Object data;
        private Object customData;
    }
}