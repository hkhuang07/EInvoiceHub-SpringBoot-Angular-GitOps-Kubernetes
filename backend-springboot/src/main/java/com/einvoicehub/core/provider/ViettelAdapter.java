package com.einvoicehub.core.provider;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Adapter cho Viettel Business Invoice - Triển khai interface InvoiceProvider
 * Viettel sử dụng REST API với JSON
 */
@Slf4j
@Service("VIETTEL")
public class ViettelAdapter implements InvoiceProvider {

    private static final String PROVIDER_CODE = "VIETTEL";
    private static final String PROVIDER_NAME = "Viettel Business Invoice";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${provider.viettel.timeout-ms:30000}")
    private int timeoutMs;

    @Value("${provider.viettel.base-url:https://ebill.vietteltelecom.vn/api/v1}")
    private String baseUrl;

    public ViettelAdapter(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-Client-Id", "EInvoiceHub")
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
            log.warn("Viettel Provider is not available: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config) {
        log.info("Viettel: Issuing invoice for request ID: {}", request.getClientRequestId());
        long startTime = System.currentTimeMillis();

        try {
            // Xây dựng payload theo format Viettel
            ViettelInvoicePayload payload = buildViettelPayload(request);

            // Gọi API
            ViettelApiResponse response = webClient
                    .post()
                    .uri("/invoices/create")
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(ViettelApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            long latency = System.currentTimeMillis() - startTime;
            log.info("Viettel: Invoice issued successfully. Latency: {}ms", latency);

            return convertToInvoiceResponse(response, InvoiceResponse.ResponseStatus.SUCCESS, latency);

        } catch (WebClientResponseException e) {
            long latency = System.currentTimeMillis() - startTime;
            log.error("Viettel: Failed to issue invoice. Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());

            ViettelApiResponse errorResponse = parseErrorResponse(e.getResponseBodyAsString());
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode(errorResponse != null ? errorResponse.getCode() : "HTTP_" + e.getStatusCode().value())
                    .errorMessage(errorResponse != null ? errorResponse.getMessage() : e.getMessage())
                    .rawResponse(errorResponse)
                    .responseTime(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            long latency = System.currentTimeMillis() - startTime;
            log.error("Viettel: Error issuing invoice", e);

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
            ViettelApiResponse response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/invoices/{invoiceNumber}")
                            .queryParam("includeDetails", false)
                            .build(invoiceNumber))
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .retrieve()
                    .bodyToMono(ViettelApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            if (response != null) {
                return mapViettelStatus(response.getStatus());
            }

        } catch (Exception e) {
            log.error("Viettel: Error getting invoice status", e);
        }

        return InvoiceStatus.FAILED;
    }

    @Override
    public InvoiceResponse cancelInvoice(String invoiceNumber, String reason, ProviderConfig config) {
        log.info("Viettel: Cancelling invoice: {}", invoiceNumber);

        try {
            ViettelCancelRequest request = ViettelCancelRequest.builder()
                    .invoiceCode(invoiceNumber)
                    .note(reason)
                    .build();

            ViettelApiResponse response = webClient
                    .post()
                    .uri("/invoices/cancel")
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ViettelApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            return convertToInvoiceResponse(response, InvoiceResponse.ResponseStatus.SUCCESS,
                    System.currentTimeMillis());

        } catch (Exception e) {
            log.error("Viettel: Error cancelling invoice", e);
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
        log.info("Viettel: Replacing invoice: {} -> new invoice", oldInvoiceNumber);

        try {
            // Viettel có API riêng cho replace
            ViettelReplaceRequest request = ViettelReplaceRequest.builder()
                    .oldInvoiceCode(oldInvoiceNumber)
                    .newInvoice(buildViettelPayload(newRequest))
                    .reason("Thay thế hóa đơn")
                    .build();

            ViettelApiResponse response = webClient
                    .post()
                    .uri("/invoices/replace")
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ViettelApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            return convertToInvoiceResponse(response, InvoiceResponse.ResponseStatus.SUCCESS,
                    System.currentTimeMillis());

        } catch (Exception e) {
            log.error("Viettel: Error replacing invoice", e);
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
            ViettelPdfResponse response = webClient
                    .get()
                    .uri("/invoices/{invoiceNumber}/pdf", invoiceNumber)
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .retrieve()
                    .bodyToMono(ViettelPdfResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            return response != null ? response.getDownloadUrl() : null;

        } catch (Exception e) {
            log.error("Viettel: Error getting PDF", e);
            return null;
        }
    }

    @Override
    public String authenticate(ProviderConfig config) {
        try {
            ViettelAuthRequest authRequest = ViettelAuthRequest.builder()
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .grantType("password")
                    .build();

            Map<String, Object> response = webClient
                    .post()
                    .uri("/auth/token")
                    .bodyValue(authRequest)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            if (response != null && response.containsKey("access_token")) {
                return (String) response.get("access_token");
            }

        } catch (Exception e) {
            log.error("Viettel: Authentication failed", e);
        }

        return null;
    }

    @Override
    public boolean testConnection(ProviderConfig config) {
        try {
            String token = authenticate(config);
            return token != null && !token.isEmpty();
        } catch (Exception e) {
            log.warn("Viettel: Connection test failed: {}", e.getMessage());
            return false;
        }
    }

    // ========== Private Helper Methods ==========

    private ViettelInvoicePayload buildViettelPayload(InvoiceRequest request) {
        List<ViettelInvoiceItem> items = request.getItems().stream()
                .map(this::convertToViettelItem)
                .collect(Collectors.toList());

        return ViettelInvoicePayload.builder()
                .invoiceType(getInvoiceTypeCode(request))
                .templateCode(getTemplateCode(request))
                .issueDate(request.getIssueDate() != null ?
                        request.getIssueDate().format(DateTimeFormatter.ISO_DATE) :
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))
                .seller(ViettelParty.builder()
                        .taxCode(request.getSeller().getTaxCode())
                        .name(request.getSeller().getName())
                        .address(request.getSeller().getAddress())
                        .phone(request.getSeller().getPhone())
                        .email(request.getSeller().getEmail())
                        .bankAccount(request.getSeller().getBankAccount())
                        .bankName(request.getSeller().getBankName())
                        .contactName(request.getSeller().getRepresentativeName())
                        .build())
                .buyer(ViettelParty.builder()
                        .taxCode(request.getBuyer().getTaxCode())
                        .name(request.getBuyer().getName())
                        .address(request.getBuyer().getAddress())
                        .phone(request.getBuyer().getPhone())
                        .email(request.getBuyer().getEmail())
                        .build())
                .items(items)
                .payment(ViettelPayment.builder()
                        .method("TM/CC")
                        .totalAmount(request.getSummary().getTotalAmount())
                        .currency(request.getSummary().getCurrencyCode())
                        .build())
                .build();
    }

    private String getInvoiceTypeCode(InvoiceRequest request) {
        if (request.getExtraConfig() != null && request.getExtraConfig().containsKey("invoiceType")) {
            return (String) request.getExtraConfig().get("invoiceType");
        }
        return "01";
    }

    private String getTemplateCode(InvoiceRequest request) {
        if (request.getExtraConfig() != null && request.getExtraConfig().containsKey("templateCode")) {
            return (String) request.getExtraConfig().get("templateCode");
        }
        return "01GTKT0/001";
    }

    private ViettelInvoiceItem convertToViettelItem(InvoiceRequest.InvoiceItem item) {
        return ViettelInvoiceItem.builder()
                .name(item.getItemName())
                .unit(item.getUnitName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .amount(item.getAmount())
                .discountAmount(item.getDiscountAmount() != null ? item.getDiscountAmount() : BigDecimal.ZERO)
                .vatRate(item.getTaxRate())
                .description(item.getDescription())
                .build();
                //.vatAmount(item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO)

    }

    private InvoiceResponse convertToInvoiceResponse(ViettelApiResponse viettelResponse,
                                                     InvoiceResponse.ResponseStatus status,
                                                     long latencyMs) {
        if (viettelResponse == null) {
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode("NULL_RESPONSE")
                    .errorMessage("No response from Viettel")
                    .responseTime(LocalDateTime.now())
                    .build();
        }

        return InvoiceResponse.builder()
                .status("200".equals(viettelResponse.getCode()) ?
                        InvoiceResponse.ResponseStatus.SUCCESS : InvoiceResponse.ResponseStatus.FAILED)
                .errorCode(viettelResponse.getCode())
                .errorMessage(viettelResponse.getMessage())
                .transactionCode(viettelResponse.getTransactionId())
                .invoiceNumber(viettelResponse.getData() != null ? viettelResponse.getData().getInvoiceCode() : null)
                .symbolCode(viettelResponse.getData() != null ? viettelResponse.getData().getInvoiceNo() : null)
                .lookupCode(viettelResponse.getData() != null ? viettelResponse.getData().getCheckSum() : null)
                .pdfUrl(viettelResponse.getData() != null ? viettelResponse.getData().getPdfUrl() : null)
                .responseTime(LocalDateTime.now())
                .rawResponse(viettelResponse)
                .build();
    }

    private InvoiceStatus mapViettelStatus(ViettelApiResponse response) {
        String viettelStatus = (response != null && response.getData() != null)
                ? response.getData().getStatus() : null;

        if (viettelStatus == null) return InvoiceStatus.FAILED;

        return switch (viettelStatus.toUpperCase()) {
            case "DA_PHAT_HANH", "SIGNED" -> InvoiceStatus.SUCCESS;
            case "DA_HUY" -> InvoiceStatus.CANCELLED;
            default -> InvoiceStatus.FAILED;
        };
    }

    private ViettelApiResponse parseErrorResponse(String body) {
        try {
            return objectMapper.readValue(body, ViettelApiResponse.class);
        } catch (Exception e) {
            return null;
        }
    }

    private ProviderConfig buildTestConfig() {
        return ProviderConfig.builder()
                .providerCode(PROVIDER_CODE)
                .username("test")
                .password("test")
                .build();
    }

    // ========== Inner Classes for Viettel API ==========

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class ViettelInvoicePayload {
        private String invoiceType;
        private String templateCode;
        private String issueDate;
        private ViettelParty seller;
        private ViettelParty buyer;
        private List<ViettelInvoiceItem> items;
        private ViettelPayment payment;
        private Map<String, Object> metadata;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class ViettelParty {
        private String taxCode;
        private String name;
        private String address;
        private String phone;
        private String email;
        private String bankAccount;
        private String bankName;
        private String contactName;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class ViettelInvoiceItem {
        private String name;
        private String unit;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount;
        private BigDecimal discountAmount;
        private BigDecimal vatRate;
        private BigDecimal vatAmount;
        private String description;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class ViettelPayment {
        private String method;
        private BigDecimal totalAmount;
        private String currency;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class ViettelApiResponse {
        private String code;
        private String message;
        private String transactionId;
        private ViettelInvoiceData data;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class ViettelInvoiceData {
        private String invoiceCode;
        private String invoiceNo;
        private String checkSum;
        private String pdfUrl;
        private String status;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class ViettelCancelRequest {
        private String invoiceCode;
        private String note;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class ViettelReplaceRequest {
        private String oldInvoiceCode;
        private ViettelInvoicePayload newInvoice;
        private String reason;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class ViettelAuthRequest {
        private String username;
        private String password;
        private String grantType;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class ViettelPdfResponse {
        private String downloadUrl;
    }
}