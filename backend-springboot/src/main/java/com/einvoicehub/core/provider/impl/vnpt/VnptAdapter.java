package com.einvoicehub.core.provider.impl.vnpt;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.provider.*;
import com.einvoicehub.core.provider.model.InvoiceRequest;
import com.einvoicehub.core.provider.model.InvoiceResponse;
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
import java.util.*;

@Slf4j
@Service("VNPT")
public class VnptAdapter implements InvoiceProvider {

    private final WebClient webClient;
    private final VnptApiMapper apiMapper;
    private final ObjectMapper objectMapper;

    @Value("${provider.vnpt.timeout-ms:30000}")
    private int timeoutMs;

    @Value("${provider.vnpt.base-url:https://api.vnptinvoice.com.vn/v1}")
    private String baseUrl;

    public VnptAdapter(WebClient.Builder webClientBuilder, VnptApiMapper apiMapper, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.apiMapper = apiMapper;
        this.objectMapper = objectMapper;
    }

    @Override public String getProviderCode() { return "VNPT"; }
    @Override public String getProviderName() { return "VNPT Invoice"; }

    @Override
    public InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config) {
        log.info("VNPT: Issuing invoice for request ID: {}", request.getClientRequestId());
        try {
            VnptInvoicePayload payload = apiMapper.toVnptPayload(request);
            VnptApiResponse response = webClient.post().uri("/invoices/publish")
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .bodyValue(payload)
                    .retrieve().bodyToMono(VnptApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs)).block();

            return apiMapper.toHubResponse(response, request.getClientRequestId());
        } catch (WebClientResponseException e) {
            VnptApiResponse errorResponse = parseErrorResponse(e.getResponseBodyAsString());
            return apiMapper.toHubResponse(errorResponse, request.getClientRequestId());
        } catch (Exception e) {
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public InvoiceStatus getInvoiceStatus(String invoiceNumber, ProviderConfig config) {
        try {
            VnptStatusResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/invoices/status").queryParam("invoiceNumber", invoiceNumber).build())
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .retrieve().bodyToMono(VnptStatusResponse.class).block();
            return (response != null) ? apiMapper.mapVnptStatus(response.getStatus()) : InvoiceStatus.FAILED;
        } catch (Exception e) { return InvoiceStatus.FAILED; }
    }

    @Override
    public InvoiceResponse cancelInvoice(String invoiceNumber, String reason, ProviderConfig config) {
        log.info("VNPT: Cancelling invoice: {}", invoiceNumber);
        try {
            VnptCancelRequest request = VnptCancelRequest.builder().invoiceNumber(invoiceNumber).reason(reason).build();
            VnptApiResponse response = webClient.post().uri("/invoices/cancel")
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .bodyValue(request)
                    .retrieve().bodyToMono(VnptApiResponse.class).block();
            return apiMapper.toHubResponse(response, null);
        } catch (Exception e) {
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public InvoiceResponse replaceInvoice(String oldInvoiceNumber, InvoiceRequest newRequest, ProviderConfig config) {
        log.info("VNPT: Replacing invoice: {}", oldInvoiceNumber);
        try {
            InvoiceResponse cancelResponse = cancelInvoice(oldInvoiceNumber, "Thay thế bởi hóa đơn mới", config);
            if (cancelResponse.isFailed()) return cancelResponse;
            return issueInvoice(newRequest, config);
        } catch (Exception e) {
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public String getInvoicePdf(String invoiceNumber, ProviderConfig config) {
        try {
            VnptPdfResponse response = webClient.get().uri("/invoices/{invoiceNumber}/pdf", invoiceNumber)
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .retrieve().bodyToMono(VnptPdfResponse.class).block();
            return response != null ? response.getPdfUrl() : null;
        } catch (Exception e) { return null; }
    }

    /**
     * KHẮC PHỤC LỖI: Triển khai phương thức lấy XML cho VNPT
     */
    @Override
    public String getInvoiceXml(String invoiceNumber, ProviderConfig config) {
        log.info("VNPT: Fetching XML for invoice: {}", invoiceNumber);
        try {
            // VNPT thường cung cấp dữ liệu XML qua endpoint tra cứu
            VnptXmlResponse response = webClient.get()
                    .uri("/invoices/{invoiceNumber}/xml", invoiceNumber)
                    .header("Authorization", "Bearer " + config.getAccessToken())
                    .retrieve()
                    .bodyToMono(VnptXmlResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            return (response != null) ? response.getXmlData() : null;
        } catch (Exception e) {
            log.error("VNPT: Error fetching XML for invoice {}", invoiceNumber, e);
            return null;
        }
    }

    @Override
    public String authenticate(ProviderConfig config) {
        try {
            VnptAuthRequest authReq = VnptAuthRequest.builder().username(config.getUsername()).password(config.getPassword()).build();
            VnptAuthResponse response = webClient.post().uri("/auth/login").bodyValue(authReq).retrieve().bodyToMono(VnptAuthResponse.class).block();
            return (response != null) ? response.getAccessToken() : null;
        } catch (Exception e) { return null; }
    }

    @Override public boolean isAvailable() { return testConnection(buildTestConfig()); }
    @Override public boolean testConnection(ProviderConfig config) { return authenticate(config) != null; }

    private VnptApiResponse parseErrorResponse(String body) {
        try { return objectMapper.readValue(body, VnptApiResponse.class); } catch (Exception e) { return null; }
    }

    private ProviderConfig buildTestConfig() { return ProviderConfig.builder().username("test").password("test").build(); }

    // --- Inner Classes ---

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class VnptInvoicePayload {
        private String invoiceType, templateCode, issueDate;
        private VnptParty seller, buyer;
        private List<VnptInvoiceDetail> details;
        private VnptSummary summary;
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class VnptParty {
        private String taxCode, companyName, address, phone, email, bankAccount, bankName, representativeName;
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class VnptInvoiceDetail {
        private String itemName, unitName, taxCategory, description;
        private BigDecimal quantity, unitPrice, amount, discountAmount, taxRate;
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class VnptSummary {
        private BigDecimal subtotalAmount, totalDiscountAmount, totalTaxAmount, totalAmount;
    }

    @lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class VnptApiResponse {
        private boolean success;
        private String errorCode, message, transactionId, invoiceNumber, fkey, lookupCode, securityCode, pdfUrl;
    }

    @lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class VnptStatusResponse { private String status, invoiceNumber; }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class VnptCancelRequest { private String invoiceNumber, reason; }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class VnptAuthRequest { private String username, password; }

    @lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class VnptAuthResponse { private String accessToken, refreshToken; private long expiresIn; }

    @lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class VnptPdfResponse { private String pdfUrl; }

    /** Thêm Class cho phản hồi XML */
    @lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class VnptXmlResponse { private String xmlData; }
}