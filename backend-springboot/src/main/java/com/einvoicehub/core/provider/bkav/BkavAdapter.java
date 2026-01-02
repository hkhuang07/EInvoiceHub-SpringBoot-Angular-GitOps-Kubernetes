package com.einvoicehub.core.provider.bkav;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.provider.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service("BKAV")
public class BkavAdapter implements InvoiceProvider {

    private final WebClient webClient;
    private final BkavApiMapper apiMapper;
    private final ObjectMapper objectMapper;

    @Value("${provider.bkav.base-url:https://einvoice.bkav.com/api/v1}")
    private String baseUrl;

    @Value("${provider.bkav.encryption-key}")
    private String encryptionKey;

    public BkavAdapter(WebClient.Builder webClientBuilder, BkavApiMapper apiMapper, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiMapper = apiMapper;
        this.objectMapper = objectMapper;
    }

    // --- Triển khai các phương thức bắt buộc của InvoiceProvider ---

    @Override
    public String getProviderCode() {
        return "BKAV";
    }

    @Override
    public String getProviderName() {
        return "Bkav eInvoice";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config) { // Sửa thành ProviderConfig
        log.info("BKAV: Issuing invoice for clientRequestId: {}", request.getClientRequestId());
        long startTime = System.currentTimeMillis();

        try {
            BkavCommandPayload payload = apiMapper.toBkavPayload(request);
            String encryptedData = encryptPayload(apiMapper.toJson(payload));

            String soapEnvelope = createSoapEnvelope(encryptedData, payload.getCmdType());

            String responseXml = webClient.post()
                    .uri("/ws/invoice.asmx")
                    .contentType(MediaType.TEXT_XML)
                    .header("PartnerGUID", config.getUsername()) // Dùng config.getUsername()
                    .header("PartnerToken", config.getPassword())
                    .bodyValue(soapEnvelope)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            return parseBkavResponse(responseXml, System.currentTimeMillis() - startTime);

        } catch (Exception e) {
            log.error("BKAV: Critical error during issuance", e);
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorMessage(e.getMessage())
                    .responseTime(LocalDateTime.now())
                    .build();
        }
    }

    // Triển khai các hàm còn lại của Interface...

    @Override
    public InvoiceStatus getInvoiceStatus(String invoiceNumber, ProviderConfig config) { return InvoiceStatus.SUCCESS; }

    @Override
    public InvoiceResponse cancelInvoice(String invoiceNumber, String reason, ProviderConfig config) { return null; }

    @Override
    public InvoiceResponse replaceInvoice(String oldInvoiceNumber, InvoiceRequest newRequest, ProviderConfig config) { return null; }

    @Override
    public String getInvoicePdf(String invoiceNumber, ProviderConfig config) { return null; }

    @Override
    public String authenticate(ProviderConfig config) { return config.getUsername(); }

    @Override
    public boolean testConnection(ProviderConfig config) { return true; }

    private String encryptPayload(String jsonData) throws Exception {
        if (encryptionKey == null || encryptionKey.isBlank()) return jsonData;

        SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

        byte[] encrypted = cipher.doFinal(jsonData.getBytes(StandardCharsets.UTF_8));

        // Sử dụng java.util.Base64 chuẩn thay cho Base64Utils
        return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(encrypted);
    }

    private String createSoapEnvelope(String encryptedData, int cmdType) {
        return String.format("""
            <?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                <soap:Body>
                    <ExecuteCommand xmlns="http://tempuri.org/">
                        <encryptedData>%s</encryptedData>
                        <cmdType>%d</cmdType>
                    </ExecuteCommand>
                </soap:Body>
            </soap:Envelope>""", encryptedData, cmdType);
    }

    private InvoiceResponse parseBkavResponse(String xml, long latency) {
        boolean isSuccess = xml.contains("<Status>200</Status>") || xml.contains("Success");
        return InvoiceResponse.builder()
                .status(isSuccess ? InvoiceResponse.ResponseStatus.SUCCESS : InvoiceResponse.ResponseStatus.FAILED)
                .errorCode(isSuccess ? "200" : "500") // Đã ép kiểu sang String
                .responseTime(LocalDateTime.now())
                .build();
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class BkavCommandPayload {
        private int cmdType;
        private BkavInvoiceData invoice;
        private List<BkavInvoiceDetail> details;
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class BkavInvoiceData {
        private String invoiceTypeId;
        private String invoiceDate;
        private String buyerName;
        private String buyerTaxCode;
        private String buyerAddress;
        private String receiverEmail;
        private String receiverMobile;
        private String currencyId;
        private double exchangeRate;
        private int payMethodId;
        private int receiveTypeId;
    }

    @lombok.Data @lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor
    public static class BkavInvoiceDetail {
        private String itemName;
        private String unitName;
        private double quantity;
        private double unitPrice;
        private double amount;
        private double taxRate;
        private int itemTypeID;
    }
}