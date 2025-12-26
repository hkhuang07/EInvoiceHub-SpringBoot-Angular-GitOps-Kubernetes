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
 * Adapter cho VNPT Invoice - Triển khai interface InvoiceProvider
 * VNPT sử dụng SOAP/XML API cho giao tiếp
 */
@Slf4j
@Service("VNPT")
public class VnptAdapter implements InvoiceProvider {

    private static final String PROVIDER_CODE = "VNPT";
    private static final String PROVIDER_NAME = "VNPT Invoice";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${provider.vnpt.timeout-ms:30000}")
    private int timeoutMs;

    @Value("${provider.vnpt.base-url:https://api.vnptinvoice.com.vn/v1}")
    private String baseUrl;

    public VnptAdapter(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .baseUrl .defaultHeader(Http(baseUrl)
                        Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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
            log.warn("VNPT Provider is not available: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config) {
        log.info("VNPT: Issuing invoice for request ID: {}", request.getClientRequestId());
        long startTime = System.currentTimeMillis();

        try {
            // Xây dựng payload theo format VNPT
            VnptInvoicePayload payload = buildVnptPayload(request);

            // Gọi API
            VnptApiResponse response = webClient
                    .post()
                    .uri("/invoices/publish")
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(VnptApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            long latency = System.currentTimeMillis() - startTime;
            log.info("VNPT: Invoice issued successfully. Latency: {}ms", latency);

            // Chuyển đổi response
            return convertToInvoiceResponse(response, InvoiceResponse.ResponseStatus.SUCCESS, latency);

        } catch (WebClientResponseException e) {
            long latency = System.currentTimeMillis() - startTime;
            log.error("VNPT: Failed to issue invoice. Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());

            VnptApiResponse errorResponse = parseErrorResponse(e.getResponseBodyAsString());
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode(errorResponse != null ? errorResponse.getErrorCode() : "HTTP_" + e.getStatusCode().value())
                    .errorMessage(errorResponse != null ? errorResponse.getMessage() : e.getMessage())
                    .rawResponse(errorResponse)
                    .responseTime(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            long latency = System.currentTimeMillis() - startTime;
            log.error("VNPT: Error issuing invoice", e);

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
            VnptStatusResponse response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/invoices/status")
                            .queryParam("invoiceNumber", invoiceNumber)
                            .build())
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .retrieve()
                    .bodyToMono(VnptStatusResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            if (response != null) {
                return mapVnptStatus(response.getStatus());
            }

        } catch (Exception e) {
            log.error("VNPT: Error getting invoice status", e);
        }

        return InvoiceStatus.FAILED;
    }

    @Override
    public InvoiceResponse cancelInvoice(String invoiceNumber, String reason, ProviderConfig config) {
        log.info("VNPT: Cancelling invoice: {}", invoiceNumber);

        try {
            VnptCancelRequest request = VnptCancelRequest.builder()
                    .invoiceNumber(invoiceNumber)
                    .reason(reason)
                    .build();

            VnptApiResponse response = webClient
                    .post()
                    .uri("/invoices/cancel")
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(VnptApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            return convertToInvoiceResponse(response, InvoiceResponse.ResponseStatus.SUCCESS,
                    System.currentTimeMillis());

        } catch (Exception e) {
            log.error("VNPT: Error cancelling invoice", e);
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
        log.info("VNPT: Replacing invoice: {} -> new invoice", oldInvoiceNumber);

        try {
            // Hủy hóa đơn cũ
            InvoiceResponse cancelResponse = cancelInvoice(oldInvoiceNumber, "Thay thế bởi hóa đơn mới", config);
            if (cancelResponse.isFailed()) {
                return cancelResponse;
            }

            // Phát hành hóa đơn mới
            return issueInvoice(newRequest, config);

        } catch (Exception e) {
            log.error("VNPT: Error replacing invoice", e);
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
            VnptPdfResponse response = webClient
                    .get()
                    .uri("/invoices/{invoiceNumber}/pdf", invoiceNumber)
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .retrieve()
                    .bodyToMono(VnptPdfResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            return response != null ? response.getPdfUrl() : null;

        } catch (Exception e) {
            log.error("VNPT: Error getting PDF", e);
            return null;
        }
    }

    @Override
    public String authenticate(ProviderConfig config) {
        try {
            VnptAuthRequest authRequest = VnptAuthRequest.builder()
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .build();

            VnptAuthResponse response = webClient
                    .post()
                    .uri("/auth/login")
                    .bodyValue(authRequest)
                    .retrieve()
                    .bodyToMono(VnptAuthResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            if (response != null && response.getAccessToken() != null) {
                return response.getAccessToken();
            }

        } catch (Exception e) {
            log.error("VNPT: Authentication failed", e);
        }

        return null;
    }

    @Override
    public boolean testConnection(ProviderConfig config) {
        try {
            String token = authenticate(config);
            return token != null && !token.isEmpty();
        } catch (Exception e) {
            log.warn("VNPT: Connection test failed: {}", e.getMessage());
            return false;
        }
    }

    // ========== Private Helper Methods ==========

    private VnptInvoicePayload buildVnptPayload(InvoiceRequest request) {
        List<VnptInvoiceDetail> details = request.getItems().stream()
                .map(this::convertToVnptDetail)
                .collect(Collectors.toList());

        return VnptInvoicePayload.builder()
                .invoiceType(request.getExtraConfig() != null ?
                        (String) request.getExtraConfig().getOrDefault("invoiceType", "01GTKT") : "01GTKT")
                .templateCode(request.getExtraConfig() != null ?
                        (String) request.getExtraConfig().getOrDefault("templateCode", "01GTKT0/001") : "01GTKT0/001")
                .issueDate(request.getIssueDate() != null ?
                        request.getIssueDate().format(DateTimeFormatter.ISO_DATE) :
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))
                .seller(VnptParty.builder()
                        .taxCode(request.getSeller().getTaxCode())
                        .companyName(request.getSeller().getName())
                        .address(request.getSeller().getAddress())
                        .phone(request.getSeller().getPhone())
                        .email(request.getSeller().getEmail())
                        .bankAccount(request.getSeller().getBankAccount())
                        .bankName(request.getSeller().getBankName())
                        .representativeName(request.getSeller().getRepresentativeName())
                        .build())
                .buyer(VnptParty.builder()
                        .taxCode(request.getBuyer().getTaxCode())
                        .companyName(request.getBuyer().getName())
                        .address(request.getBuyer().getAddress())
                        .phone(request.getBuyer().getPhone())
                        .email(request.getBuyer().getEmail())
                        .build())
                .details(details)
                .summary(VnptSummary.builder()
                        .subtotalAmount(request.getSummary().getSubtotalAmount())
                        .totalDiscountAmount(request.getSummary().getTotalDiscountAmount())
                        .totalTaxAmount(request.getSummary().getTotalTaxAmount())
                        .totalAmount(request.getSummary().getTotalAmount())
                        .build())
                .build();
    }

    private VnptInvoiceDetail convertToVnptDetail(InvoiceRequest.InvoiceItem item) {
        return VnptInvoiceDetail.builder()
                .itemName(item.getItemName())
                .unitName(item.getUnitName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .amount(item.getAmount())
                .discountAmount(item.getDiscountAmount())
                .taxRate(item.getTaxRate())
                .taxCategory(item.getTaxCategory() != null ? item.getTaxCategory() : "5")
                .description(item.getDescription())
                .build();
    }

    private InvoiceResponse convertToInvoiceResponse(VnptApiResponse vnptResponse,
                                                     InvoiceResponse.ResponseStatus status,
                                                     long latencyMs) {
        if (vnptResponse == null) {
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorCode("NULL_RESPONSE")
                    .errorMessage("No response from VNPT")
                    .responseTime(LocalDateTime.now())
                    .build();
        }

        return InvoiceResponse.builder()
                .status(vnptResponse.isSuccess() ? InvoiceResponse.ResponseStatus.SUCCESS :
                        InvoiceResponse.ResponseStatus.FAILED)
                .errorCode(vnptResponse.getErrorCode())
                .errorMessage(vnptResponse.getMessage())
                .transactionCode(vnptResponse.getTransactionId())
                .invoiceNumber(vnptResponse.getInvoiceNumber())
                .symbolCode(vnptResponse.getFkey() != null ?
                        vnptResponse.getFkey().substring(0, Math.min(10, vnptResponse.getFkey().length())) : null)
                .templateCode(vnptResponse.getFkey())
                .lookupCode(vnptResponse.getLookupCode())
                .securityCode(vnptResponse.getSecurityCode())
                .pdfUrl(vnptResponse.getPdfUrl())
                .responseTime(LocalDateTime.now())
                .rawResponse(vnptResponse)
                .build();
    }

    private InvoiceStatus mapVnptStatus(String vnptStatus) {
        if (vnptStatus == null) return InvoiceStatus.FAILED;

        return switch (vnptStatus.toUpperCase()) {
            case "ISSUED", "SUCCESS" -> InvoiceStatus.SUCCESS;
            case "PENDING", "PROCESSING" -> InvoiceStatus.SENT_TO_PROVIDER;
            case "CANCELLED" -> InvoiceStatus.CANCELLED;
            case "REPLACED" -> InvoiceStatus.REPLACED;
            default -> InvoiceStatus.FAILED;
        };
    }

    private VnptApiResponse parseErrorResponse(String body) {
        try {
            return objectMapper.readValue(body, VnptApiResponse.class);
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

    // ========== Inner Classes for VNPT API ==========

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class VnptInvoicePayload {
        private String invoiceType;
        private String templateCode;
        private String issueDate;
        private VnptParty seller;
        private VnptParty buyer;
        private List<VnptInvoiceDetail> details;
        private VnptSummary summary;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class VnptParty {
        private String taxCode;
        private String companyName;
        private String address;
        private String phone;
        private String email;
        private String bankAccount;
        private String bankName;
        private String representativeName;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class VnptInvoiceDetail {
        private String itemName;
        private String unitName;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount;
        private BigDecimal discountAmount;
        private BigDecimal taxRate;
        private String taxCategory;
        private String description;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class VnptSummary {
        private BigDecimal subtotalAmount;
        private BigDecimal totalDiscountAmount;
        private BigDecimal totalTaxAmount;
        private BigDecimal totalAmount;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class VnptApiResponse {
        private boolean success;
        private String errorCode;
        private String message;
        private String transactionId;
        private String invoiceNumber;
        private String fkey;
        private String lookupCode;
        private String securityCode;
        private String pdfUrl;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class VnptStatusResponse {
        private String status;
        private String invoiceNumber;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class VnptCancelRequest {
        private String invoiceNumber;
        private String reason;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class VnptAuthRequest {
        private String username;
        private String password;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class VnptAuthResponse {
        private String accessToken;
        private String refreshToken;
        private long expiresIn;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class VnptPdfResponse {
        private String pdfUrl;
    }
}