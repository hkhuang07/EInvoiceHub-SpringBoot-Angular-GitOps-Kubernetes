package com.einvoicehub.core.provider.misa;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.provider.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode; // Khắc phục lỗi Missing RoundingMode
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service("MISA")
public class MisaAdapter implements InvoiceProvider {

    private final WebClient webClient;
    private final MisaApiMapper apiMapper;
    private final ObjectMapper objectMapper;

    @Value("${provider.misa.base-url:https://api.meinvoice.vn/api/integration}")
    private String baseUrl;

    public MisaAdapter(WebClient.Builder webClientBuilder, MisaApiMapper apiMapper, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiMapper = apiMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getProviderCode() { return "MISA"; }

    @Override
    public String getProviderName() { return "MISA MeInvoice"; }

    @Override
    public boolean isAvailable() { return true; }

    @Override
    public InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config) {
        log.info("MISA: Issuing invoice for request ID: {}", request.getClientRequestId());
        try {
            String token = authenticate(config);
            MisaInvoicePayload payload = apiMapper.toMisaPayload(request);
            calculateTotals(payload); // Tự động tính toán tổng tiền

            Map<String, Object> body = Map.of(
                    "SignType", 2,
                    "InvoiceData", Collections.singletonList(payload)
            );

            MisaApiResponse response = webClient.post()
                    .uri("/invoice")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(MisaApiResponse.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            return apiMapper.toInvoiceResponse(response, request.getClientRequestId());
        } catch (Exception e) {
            log.error("MISA: Issuance failed", e);
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public InvoiceStatus getInvoiceStatus(String invoiceNumber, ProviderConfig config) {
        try {
            String token = authenticate(config);
            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>(); // Sửa lỗi queryParams
            queryParams.add("invoiceId", invoiceNumber);

            MisaApiResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/invoice/status").queryParams(queryParams).build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .bodyToMono(MisaApiResponse.class)
                    .block();

            return (response != null && response.isSuccess()) ? InvoiceStatus.SUCCESS : InvoiceStatus.FAILED;
        } catch (Exception e) {
            return InvoiceStatus.FAILED;
        }
    }

    @Override
    public String authenticate(ProviderConfig config) {
        Map<String, String> authReq = Map.of(
                "username", config.getUsername(),
                "password", config.getPassword(),
                "taxcode", config.getUsername() // Giả định AppID là MST
        );
        Map<String, Object> res = webClient.post().uri("/auth/token").bodyValue(authReq).retrieve().bodyToMono(Map.class).block();
        return res != null ? res.get("Data").toString() : null;
    }

    // --- Helper logic cho MISA ---
    private void calculateTotals(MisaInvoicePayload payload) {
        BigDecimal totalAmount = payload.getOriginalInvoiceDetail().stream()
                .map(d -> d.getAmountOC().subtract(d.getDiscountAmountOC()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        payload.setTotalAmountOC(totalAmount.setScale(2, RoundingMode.HALF_UP));
        payload.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
    }

    @Override public InvoiceResponse cancelInvoice(String no, String r, ProviderConfig c) { return null; }
    @Override public InvoiceResponse replaceInvoice(String o, InvoiceRequest n, ProviderConfig c) { return null; }
    @Override public String getInvoicePdf(String n, ProviderConfig c) { return null; }
    @Override public boolean testConnection(ProviderConfig c) { return true; }

    // --- Inner Classes với đầy đủ Lombok ---
    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class MisaInvoicePayload {
        private String refId, invSeries, invDate, currencyCode, paymentMethodName, buyerLegalName, buyerTaxCode, buyerAddress, buyerEmail, buyerPhoneNumber;
        private BigDecimal exchangeRate, totalAmountOC, totalAmount;
        private List<MisaInvoiceDetail> originalInvoiceDetail;
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class MisaInvoiceDetail {
        private Integer itemType;
        private String itemName, unitName, vatRateName;
        private BigDecimal quantity, unitPrice, amountOC, discountAmountOC;
    }

    @lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class MisaApiResponse {
        private boolean success;
        private String errorCode;
        private String descriptionErrorCode;
    }
}