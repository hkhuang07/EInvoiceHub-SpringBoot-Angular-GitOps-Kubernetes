package com.einvoicehub.core.provider.impl.viettel;

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
@Service("VIETTEL")
public class ViettelAdapter implements InvoiceProvider {

    private final WebClient webClient;
    private final ViettelApiMapper apiMapper;
    private final ObjectMapper objectMapper;

    @Value("${provider.viettel.timeout-ms:30000}")
    private int timeoutMs;

    public ViettelAdapter(WebClient.Builder webClientBuilder,
                          ViettelApiMapper apiMapper,
                          ObjectMapper objectMapper,
                          @Value("${provider.viettel.base-url:https://ebill.vietteltelecom.vn/api/v1}") String baseUrl) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-Client-Id", "EInvoiceHub")
                .build();
        this.apiMapper = apiMapper;
        this.objectMapper = objectMapper;
    }

    @Override public String getProviderCode() { return "VIETTEL"; }
    @Override public String getProviderName() { return "Viettel Business Invoice"; }
    @Override public boolean isAvailable() { return true; }

    @Override
    public InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config) {
        log.info("Viettel: Issuing invoice for ClientRequestID: {}", request.getClientRequestId());
        try {
            ViettelInvoicePayload payload = apiMapper.toViettelPayload(request);
            ViettelApiResponse response = webClient.post().uri("/invoices/create")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getAccessToken())
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(ViettelApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            return apiMapper.toHubResponse(response, request.getClientRequestId());
        } catch (WebClientResponseException e) {
            return apiMapper.toHubResponse(parseError(e.getResponseBodyAsString()), request.getClientRequestId());
        } catch (Exception e) {
            log.error("Viettel: Issuance failed: {}", e.getMessage());
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public InvoiceStatus getInvoiceStatus(String invoiceNumber, ProviderConfig config) {
        try {
            ViettelApiResponse response = webClient.get()
                    .uri(uri -> uri.path("/invoices/{id}").queryParam("includeDetails", false).build(invoiceNumber))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getAccessToken())
                    .retrieve()
                    .bodyToMono(ViettelApiResponse.class)
                    .block();
            return apiMapper.mapViettelStatus(response);
        } catch (Exception e) {
            log.error("Viettel: Status check failed for {}: {}", invoiceNumber, e.getMessage());
            return InvoiceStatus.FAILED;
        }
    }

    @Override
    public InvoiceResponse cancelInvoice(String invoiceNumber, String reason, ProviderConfig config) {
        try {
            ViettelCancelRequest body = ViettelCancelRequest.builder().invoiceCode(invoiceNumber).note(reason).build();
            ViettelApiResponse response = webClient.post().uri("/invoices/cancel")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getAccessToken())
                    .bodyValue(body)
                    .retrieve().bodyToMono(ViettelApiResponse.class).block();
            return apiMapper.toHubResponse(response, null);
        } catch (Exception e) {
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public InvoiceResponse replaceInvoice(String oldNo, InvoiceRequest newReq, ProviderConfig config) {
        try {
            ViettelReplaceRequest body = ViettelReplaceRequest.builder()
                    .oldInvoiceCode(oldNo)
                    .newInvoice(apiMapper.toViettelPayload(newReq))
                    .reason("Invoice replacement")
                    .build();
            ViettelApiResponse response = webClient.post().uri("/invoices/replace")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getAccessToken())
                    .bodyValue(body)
                    .retrieve().bodyToMono(ViettelApiResponse.class).block();
            return apiMapper.toHubResponse(response, newReq.getClientRequestId());
        } catch (Exception e) {
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public String getInvoicePdf(String invoiceNumber, ProviderConfig config) {
        try {
            ViettelPdfResponse res = webClient.get().uri("/invoices/{id}/pdf", invoiceNumber)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getAccessToken())
                    .retrieve().bodyToMono(ViettelPdfResponse.class).block();
            return res != null ? res.getDownloadUrl() : null;
        } catch (Exception e) { return null; }
    }

    @Override
    public String getInvoiceXml(String invoiceNumber, ProviderConfig config) {
        try {
            ViettelXmlResponse res = webClient.get().uri("/invoices/{id}/xml", invoiceNumber)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getAccessToken())
                    .retrieve().bodyToMono(ViettelXmlResponse.class).block();
            return res != null ? res.getXmlData() : null;
        } catch (Exception e) {
            log.error("Viettel: XML retrieval failed: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String authenticate(ProviderConfig config) {
        try {
            ViettelAuthRequest auth = ViettelAuthRequest.builder()
                    .username(config.getUsername()).password(config.getPassword()).grantType("password").build();
            Map<?, ?> res = webClient.post().uri("/auth/token").bodyValue(auth).retrieve().bodyToMono(Map.class).block();
            return res != null ? (String) res.get("access_token") : null;
        } catch (Exception e) { return null; }
    }

    @Override public boolean testConnection(ProviderConfig config) { return authenticate(config) != null; }

    private ViettelApiResponse parseError(String body) {
        try { return objectMapper.readValue(body, ViettelApiResponse.class); } catch (Exception e) { return null; }
    }

    /* Inner Data Models */
    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class ViettelInvoicePayload {
        private String invoiceType, templateCode, issueDate;
        private ViettelParty seller, buyer;
        private List<ViettelInvoiceItem> items;
        private ViettelPayment payment;
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class ViettelParty {
        private String taxCode, name, address, phone, email, bankAccount, bankName, contactName;
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class ViettelInvoiceItem {
        private String name, unit, description;
        private BigDecimal quantity, unitPrice, amount, discountAmount, vatRate, vatAmount;
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class ViettelPayment { private String method, currency; private BigDecimal totalAmount; }

    @lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class ViettelApiResponse { private String code, message, transactionId; private ViettelInvoiceData data; }

    @lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class ViettelInvoiceData { private String invoiceCode, invoiceNo, checkSum, pdfUrl, status; }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class ViettelCancelRequest { private String invoiceCode, note; }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class ViettelReplaceRequest { private String oldInvoiceCode; private ViettelInvoicePayload newInvoice; private String reason; }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class ViettelAuthRequest { private String username, password, grantType; }

    @lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class ViettelPdfResponse { private String downloadUrl; }

    @lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class ViettelXmlResponse { private String xmlData; }
}