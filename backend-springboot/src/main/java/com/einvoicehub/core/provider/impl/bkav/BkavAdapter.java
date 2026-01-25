package com.einvoicehub.core.provider.impl.bkav;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.provider.*;
import com.einvoicehub.core.provider.model.InvoiceRequest;
import com.einvoicehub.core.provider.model.InvoiceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service("BKAV")
public class BkavAdapter implements InvoiceProvider {

    private final WebClient webClient;
    private final BkavApiMapper apiMapper;
    private final ObjectMapper objectMapper;

    @Value("${provider.bkav.timeout-ms:30000}")
    private int timeoutMs;

    @Value("${provider.bkav.encryption-key:}")
    private String encryptionKey;

    public BkavAdapter(WebClient.Builder webClientBuilder,
                       BkavApiMapper apiMapper,
                       ObjectMapper objectMapper,
                       @Value("${provider.bkav.base-url:https://einvoice.bkav.com/api/v1}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.apiMapper = apiMapper;
        this.objectMapper = objectMapper;
    }

    @Override public String getProviderCode() { return "BKAV"; }
    @Override public String getProviderName() { return "Bkav eInvoice"; }
    @Override public boolean isAvailable() { return true; }

    @Override
    public InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config) {
        log.info("BKAV: Issuing invoice for ClientID: {}", request.getClientRequestId());
        try {
            BkavCommandPayload payload = apiMapper.toBkavPayload(request);
            String encryptedData = encryptPayload(payload);

            BkavApiResponse response = callBkavApi(encryptedData, config);
            return apiMapper.toHubResponse(response, request.getClientRequestId());
        } catch (Exception e) {
            log.error("BKAV Issuance failed: {}", e.getMessage());
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorMessage("Issuance error: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public InvoiceStatus getInvoiceStatus(String invoiceNumber, ProviderConfig config) {
        try {
            BkavCommandPayload payload = BkavCommandPayload.builder()
                    .cmdType(850)
                    .commandObject(invoiceNumber)
                    .build();

            BkavApiResponse response = callBkavApi(encryptPayload(payload), config);
            return (response != null) ? apiMapper.mapBkavStatus(response) : InvoiceStatus.FAILED;
        } catch (Exception e) {
            log.error("BKAV status check failed for {}: {}", invoiceNumber, e.getMessage());
            return InvoiceStatus.FAILED;
        }
    }

    @Override
    public InvoiceResponse cancelInvoice(String invoiceNumber, String reason, ProviderConfig config) {
        try {
            BkavCommandPayload payload = BkavCommandPayload.builder()
                    .cmdType(201)
                    .commandObject(List.of(Map.of("InvoiceGUID", invoiceNumber, "Reason", reason)))
                    .build();

            BkavApiResponse response = callBkavApi(encryptPayload(payload), config);
            return apiMapper.toHubResponse(response, null);
        } catch (Exception e) {
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    @Override
    public InvoiceResponse replaceInvoice(String oldNo, InvoiceRequest newReq, ProviderConfig config) {
        try {
            BkavCommandPayload payload = apiMapper.toBkavPayload(newReq);
            payload.setCmdType(120);

            if (payload.getCommandObject() instanceof List<?> list && !list.isEmpty()) {
                Map<String, Object> invoice = (Map<String, Object>) list.get(0);
                invoice.put("OriginalInvoiceIdentify", String.format("[1]_[%s]_[%s]",
                        invoice.getOrDefault("InvoiceSerial", ""), formatInvoiceNo(oldNo)));
            }

            BkavApiResponse response = callBkavApi(encryptPayload(payload), config);
            return apiMapper.toHubResponse(response, newReq.getClientRequestId());
        } catch (Exception e) {
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public String getInvoicePdf(String invoiceNumber, ProviderConfig config) {
        try {
            BkavCommandPayload payload = BkavCommandPayload.builder()
                    .cmdType(816)
                    .commandObject(List.of(Map.of("PartnerInvoiceID", invoiceNumber, "PartnerInvoiceStringID", "")))
                    .build();

            BkavApiResponse response = callBkavApi(encryptPayload(payload), config);
            return (response != null) ? response.getCode() : null;
        } catch (Exception e) { return null; }
    }

    @Override
    public String getInvoiceXml(String invoiceNumber, ProviderConfig config) {
        try {
            BkavCommandPayload payload = BkavCommandPayload.builder()
                    .cmdType(817)
                    .commandObject(List.of(Map.of("PartnerInvoiceID", invoiceNumber, "PartnerInvoiceStringID", "")))
                    .build();

            BkavApiResponse response = callBkavApi(encryptPayload(payload), config);
            return (response != null && response.getObject() != null) ? response.getObject().toString() : null;
        } catch (Exception e) {
            log.error("BKAV: Failed to fetch XML for {}", invoiceNumber);
            return null;
        }
    }

    @Override public String authenticate(ProviderConfig config) { return config.getUsername(); }
    @Override public boolean testConnection(ProviderConfig config) { return true; }

    /* Helper to call BKAV API with standardized headers */
    private BkavApiResponse callBkavApi(String encryptedData, ProviderConfig config) {
        return webClient.post().uri("/exec-command")
                .header("PartnerGUID", config.getUsername())
                .header("PartnerToken", config.getPassword())
                .bodyValue(Map.of("CommandData", encryptedData))
                .retrieve()
                .bodyToMono(BkavApiResponse.class)
                .timeout(Duration.ofMillis(timeoutMs))
                .block();
    }

    private String encryptPayload(BkavCommandPayload payload) throws Exception {
        String jsonData = objectMapper.writeValueAsString(payload);
        if (!StringUtils.hasText(encryptionKey)) return jsonData;

        SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
        byte[] encrypted = cipher.doFinal(jsonData.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(encrypted);
    }

    private String formatInvoiceNo(String no) {
        try { return String.format("%08d", Integer.parseInt(no)); } catch (Exception e) { return no; }
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class BkavCommandPayload { private int cmdType; private Object commandObject; }

    @lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class BkavApiResponse {
        private int status;
        private Object object;
        private boolean isOk, isError;
        private String code;
    }
}