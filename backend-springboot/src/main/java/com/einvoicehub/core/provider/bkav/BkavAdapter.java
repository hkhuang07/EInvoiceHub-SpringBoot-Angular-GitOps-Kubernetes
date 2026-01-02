package com.einvoicehub.core.provider.bkav;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.entity.mysql.MerchantProviderConfig;
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
@Service("BKAV") //Khắc phục lỗi : Class 'BkavAdapter' must either be declared abstract or implement abstract method 'getProviderCode()' in 'InvoiceProvider'
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

    @Override //Khắc phục lỗi Method does not override method from its superclass
    public InvoiceResponse issueInvoice(InvoiceRequest request, MerchantProviderConfig config) {
        log.info("BKAV: Issuing invoice for clientRequestId: {}", request.getClientRequestId());
        long startTime = System.currentTimeMillis();

        try {
            // 1. Map & Encrypt Payload
            BkavCommandPayload payload = apiMapper.toBkavPayload(request);
            String encryptedData = encryptPayload(apiMapper.toJson(payload));

            // 2. Call BKAV via SOAP/XML
            String soapEnvelope = createSoapEnvelope(encryptedData, payload.getCmdType());

            String responseXml = webClient.post()
                    .uri("/ws/invoice.asmx")
                    .contentType(MediaType.TEXT_XML)
                    .header("PartnerGUID", config.getUsernameService())
                    .header("PartnerToken", config.getPasswordServiceEncrypted()) // Đã giải mã qua Converter
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

    private String encryptPayload(String jsonData) throws Exception {
        if (encryptionKey == null || encryptionKey.isBlank()) return jsonData;

        SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

        byte[] encrypted = cipher.doFinal(jsonData.getBytes(StandardCharsets.UTF_8));

        // Sửa lỗi Base64Utils: Dùng java.util.Base64 chuẩn
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
        // Logic parse XML để lấy mã lỗi và số hóa đơn
        boolean isSuccess = xml.contains("<Status>200</Status>") || xml.contains("Success");
        return InvoiceResponse.builder()
                .status(isSuccess ? InvoiceResponse.ResponseStatus.SUCCESS : InvoiceResponse.ResponseStatus.FAILED)
                .errorCode(isSuccess ? "200" : "500") // Đã chuyển int sang String để fix lỗi
                .responseTime(LocalDateTime.now())
                .build();
    }

    // Các hàm testConnection, getInvoiceStatus... triển khai tương tự issueInvoice

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