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

    @Value("${provider.bkav.base-url:https://einvoice.bkav.com/api/v1}")
    private String baseUrl;

    @Value("${provider.bkav.encryption-key:}")
    private String encryptionKey;

    public BkavAdapter(WebClient.Builder webClientBuilder, BkavApiMapper apiMapper, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        this.apiMapper = apiMapper;
        this.objectMapper = objectMapper;
    }

    @Override public String getProviderCode() { return "BKAV"; }
    @Override public String getProviderName() { return "Bkav eInvoice"; }

    @Override
    public InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config) {
        log.info("BKAV: Issuing invoice for request ID: {}", request.getClientRequestId());
        try {
            BkavCommandPayload payload = apiMapper.toBkavPayload(request);
            String encryptedData = encryptPayload(payload);

            BkavApiResponse response = webClient.post().uri("/exec-command")
                    .header("PartnerGUID", config.getUsername())
                    .header("PartnerToken", config.getPassword())
                    .bodyValue(Map.of("CommandData", encryptedData))
                    .retrieve().bodyToMono(BkavApiResponse.class)
                    .timeout(Duration.ofMillis(timeoutMs)).block();

            return apiMapper.toHubResponse(response, request.getClientRequestId());
        } catch (Exception e) {
            log.error("BKAV issuance error", e);
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public InvoiceStatus getInvoiceStatus(String invoiceNumber, ProviderConfig config) {
        try {
            BkavCommandPayload payload = BkavCommandPayload.builder().cmdType(850).commandObject(invoiceNumber).build();
            String encryptedData = encryptPayload(payload);
            BkavApiResponse response = webClient.post().uri("/exec-command")
                    .header("PartnerGUID", config.getUsername()).header("PartnerToken", config.getPassword())
                    .bodyValue(Map.of("CommandData", encryptedData))
                    .retrieve().bodyToMono(BkavApiResponse.class).block();
            return (response != null) ? apiMapper.mapBkavStatus(response) : InvoiceStatus.FAILED;
        } catch (Exception e) { return InvoiceStatus.FAILED; }
    }

    @Override
    public InvoiceResponse cancelInvoice(String invoiceNumber, String reason, ProviderConfig config) {
        try {
            BkavCommandPayload payload = BkavCommandPayload.builder().cmdType(201)
                    .commandObject(Collections.singletonList(Map.of("InvoiceGUID", invoiceNumber, "Reason", reason))).build();
            String encryptedData = encryptPayload(payload);
            BkavApiResponse response = webClient.post().uri("/exec-command")
                    .header("PartnerGUID", config.getUsername()).header("PartnerToken", config.getPassword())
                    .bodyValue(Map.of("CommandData", encryptedData)).retrieve().bodyToMono(BkavApiResponse.class).block();
            return apiMapper.toHubResponse(response, null);
        } catch (Exception e) { return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage(e.getMessage()).build(); }
    }

    @Override
    public InvoiceResponse replaceInvoice(String oldInvoiceNumber, InvoiceRequest newRequest, ProviderConfig config) {
        try {
            BkavCommandPayload payload = apiMapper.toBkavPayload(newRequest);
            payload.setCmdType(120);

            if (payload.getCommandObject() instanceof List) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) payload.getCommandObject();
                if (!list.isEmpty()) {
                    list.get(0).put("OriginalInvoiceIdentify", String.format("[1]_[%s]_[%s]",
                            list.get(0).getOrDefault("InvoiceSerial", ""), formatInvoiceNo(oldInvoiceNumber)));
                }
            }

            String encryptedData = encryptPayload(payload);
            BkavApiResponse response = webClient.post().uri("/exec-command")
                    .header("PartnerGUID", config.getUsername()).header("PartnerToken", config.getPassword())
                    .bodyValue(Map.of("CommandData", encryptedData)).retrieve().bodyToMono(BkavApiResponse.class).block();
            return apiMapper.toHubResponse(response, newRequest.getClientRequestId());
        } catch (Exception e) { return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage(e.getMessage()).build(); }
    }

    @Override
    public String getInvoicePdf(String invoiceNumber, ProviderConfig config) {
        try {
            BkavCommandPayload payload = BkavCommandPayload.builder().cmdType(816)
                    .commandObject(Collections.singletonList(Map.of("PartnerInvoiceID", invoiceNumber, "PartnerInvoiceStringID", ""))).build();
            String encryptedData = encryptPayload(payload);
            BkavApiResponse response = webClient.post().uri("/exec-command")
                    .header("PartnerGUID", config.getUsername()).header("PartnerToken", config.getPassword())
                    .bodyValue(Map.of("CommandData", encryptedData)).retrieve().bodyToMono(BkavApiResponse.class).block();
            return (response != null) ? baseUrl + response.getCode() : null;
        } catch (Exception e) { return null; }
    }

    /**
     * KHẮC PHỤC LỖI: Implement phương thức lấy XML cho BKAV
     * BKAV thường dùng command tương tự tải PDF nhưng trả về dữ liệu XML thô hoặc Base64.
     */
    @Override
    public String getInvoiceXml(String invoiceNumber, ProviderConfig config) {
        log.info("BKAV: Getting XML for invoice: {}", invoiceNumber);
        try {
            // Sử dụng command 817 (ví dụ) để lấy dữ liệu XML thô từ BKAV
            BkavCommandPayload payload = BkavCommandPayload.builder().cmdType(817)
                    .commandObject(Collections.singletonList(Map.of("PartnerInvoiceID", invoiceNumber, "PartnerInvoiceStringID", ""))).build();
            String encryptedData = encryptPayload(payload);

            BkavApiResponse response = webClient.post().uri("/exec-command")
                    .header("PartnerGUID", config.getUsername()).header("PartnerToken", config.getPassword())
                    .bodyValue(Map.of("CommandData", encryptedData))
                    .retrieve().bodyToMono(BkavApiResponse.class).block();

            // Trả về dữ liệu XML nằm trong trường object của response
            return (response != null && response.getObject() != null) ? response.getObject().toString() : null;
        } catch (Exception e) {
            log.error("BKAV: Error fetching XML for invoice {}", invoiceNumber, e);
            return null;
        }
    }

    @Override public String authenticate(ProviderConfig config) { return config.getUsername(); }
    @Override public boolean isAvailable() { return true; }
    @Override public boolean testConnection(ProviderConfig config) { return true; }

    private String encryptPayload(BkavCommandPayload payload) throws Exception {
        String jsonData = objectMapper.writeValueAsString(payload);
        if (!StringUtils.hasText(encryptionKey)) return jsonData;

        SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        byte[] iv = new byte[12]; new SecureRandom().nextBytes(iv);
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
    public static class BkavApiResponse { private int status; private Object object; private boolean isOk, isError; private String code; }
}