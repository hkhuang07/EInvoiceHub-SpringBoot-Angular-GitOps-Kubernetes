package com.einvoicehub.core.provider.impl.misa;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.provider.*;
import com.einvoicehub.core.provider.model.InvoiceRequest;
import com.einvoicehub.core.provider.model.InvoiceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
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

    @Value("${provider.misa.test-base-url:https://testapi.meinvoice.vn/api/integration}")
    private String testBaseUrl;

    private String cachedToken;
    private LocalDateTime tokenExpiry;

    public MisaAdapter(WebClient.Builder webClientBuilder, MisaApiMapper apiMapper, ObjectMapper objectMapper) {
        // Trong môi trường dev, chúng ta dùng testBaseUrl
        this.webClient = webClientBuilder.baseUrl(testBaseUrl).build();
        this.apiMapper = apiMapper;
        this.objectMapper = objectMapper;
    }

    @Override public String getProviderCode() { return "MISA"; }
    @Override public String getProviderName() { return "MISA MeInvoice"; }

    @Override
    public InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config) {
        log.info("MISA: Issuing invoice for request ID: {}", request.getClientRequestId());
        try {
            String token = getOrRefreshToken(config);
            MisaInvoicePayload payload = apiMapper.toMisaPayload(request);

            int signType = 2; // HSM mặc định
            Map<String, Object> body = Map.of("SignType", signType, "InvoiceData", Collections.singletonList(payload));

            MisaApiResponse response = webClient.post().uri("/invoice")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(body)
                    .retrieve().bodyToMono(MisaApiResponse.class)
                    .timeout(Duration.ofSeconds(30)).block();

            return apiMapper.toHubResponse(response, request.getClientRequestId());
        } catch (Exception e) {
            log.error("MISA: Issuance error", e);
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public InvoiceStatus getInvoiceStatus(String transactionId, ProviderConfig config) {
        try {
            String token = getOrRefreshToken(config);
            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            queryParams.add("invoiceWithCode", "true");
            queryParams.add("inputType", "1");

            List<String> requestBody = Collections.singletonList(transactionId);

            MisaApiResponse response = webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/invoice/status").queryParams(queryParams).build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(requestBody)
                    .retrieve().bodyToMono(MisaApiResponse.class).block();

            if (response != null && response.getData() instanceof List) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) response.getData();
                if (!data.isEmpty()) return apiMapper.mapMisaStatus((int) data.get(0).get("EInvoiceStatus"));
            }
        } catch (Exception e) { log.error("MISA status check failed", e); }
        return InvoiceStatus.FAILED;
    }

    @Override
    public String authenticate(ProviderConfig config) {
        Map<String, Object> authReq = Map.of(
                "appid", config.getAppId() != null ? config.getAppId() : config.getUsername(),
                "taxcode", config.getUsername(),
                "username", config.getUsername(),
                "password", config.getPassword()
        );
        try {
            Map res = webClient.post().uri("/auth/token").bodyValue(authReq).retrieve().bodyToMono(Map.class).block();
            return (res != null && res.get("Data") != null) ? res.get("Data").toString() : null;
        } catch (Exception e) {
            log.error("MISA Authentication failed", e);
            return null;
        }
    }

    private String getOrRefreshToken(ProviderConfig config) {
        if (cachedToken != null && tokenExpiry != null && tokenExpiry.isAfter(LocalDateTime.now().plusMinutes(5))) return cachedToken;
        String token = authenticate(config);
        if (token != null) { cachedToken = token; tokenExpiry = LocalDateTime.now().plusDays(7); return token; }
        throw new RuntimeException("MISA Auth Failed");
    }

    /**
     * KHẮC PHỤC LỖI: Triển khai phương thức lấy XML cho MISA
     */
    @Override
    public String getInvoiceXml(String invoiceNumber, ProviderConfig config) {
        log.info("MISA: Fetching XML for invoice: {}", invoiceNumber);
        try {
            String token = getOrRefreshToken(config);
            // MISA API thường yêu cầu InvoiceGUID (transactionId) để lấy dữ liệu XML
            Map<String, String> requestBody = Map.of("InvoiceGUID", invoiceNumber);

            MisaApiResponse response = webClient.post()
                    .uri("/invoice/export-xml")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(MisaApiResponse.class)
                    .block();

            return (response != null && response.getData() != null) ? response.getData().toString() : null;
        } catch (Exception e) {
            log.error("MISA: Error fetching XML for invoice {}", invoiceNumber, e);
            return null;
        }
    }

    @Override public InvoiceResponse cancelInvoice(String no, String r, ProviderConfig c) { return null; }
    @Override public InvoiceResponse replaceInvoice(String o, InvoiceRequest n, ProviderConfig c) { return null; }
    @Override public String getInvoicePdf(String n, ProviderConfig c) { return null; }
    @Override public boolean isAvailable() { return true; }
    @Override public boolean testConnection(ProviderConfig c) { return authenticate(c) != null; }

    // --- Inner Classes ---
    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class MisaInvoicePayload {
        private String refId, invSeries, invDate, currencyCode, paymentMethodName, buyerLegalName, buyerTaxCode, buyerAddress, buyerFullName, buyerPhoneNumber, buyerEmail, totalAmountInWords;
        private BigDecimal exchangeRate, totalSaleAmountOC, totalSaleAmount, totalAmountOC, totalAmount, totalVATAmountOC;
        private boolean isInvoiceSummary;
        private List<MisaInvoiceDetail> originalInvoiceDetail;
        private List<MisaTaxRateInfo> taxRateInfo;
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class MisaInvoiceDetail {
        private Integer itemType, sortOrder, lineNumber;
        private String itemName, unitName, vatRateName;
        private BigDecimal quantity, unitPrice, amountOC, amount, discountAmountOC, discountAmount, vatAmountOC, vatAmount;
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class MisaTaxRateInfo {
        private String vatRateName;
        private BigDecimal amountWithoutVATOC, vatAmountOC;
    }

    @lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class MisaApiResponse {
        private Boolean success;
        private String errorCode, descriptionErrorCode;
        private List<Map<String, Object>> publishInvoiceResult;
        private Object data;
    }
}