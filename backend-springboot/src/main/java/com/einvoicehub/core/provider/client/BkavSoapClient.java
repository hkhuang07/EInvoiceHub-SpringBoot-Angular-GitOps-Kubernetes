package com.einvoicehub.core.provider.client;

import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j  
@RequiredArgsConstructor
public class BkavSoapClient {

    private final RestTemplate restTemplate; // Đã cấu hình timeout trong Bean config

    public String sendSoapRequest(String url, String soapAction, String xmlBody) {
        String soapEnvelope =
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                        "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                        "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "  <soap:Body>" +
                        "    <" + soapAction + " xmlns=\"http://tempuri.org/\">" +
                        "      <encodedXML>" + xmlBody + "</encodedXML>" +
                        "    </" + soapAction + ">" +
                        "  </soap:Body>" +
                        "</soap:Envelope>";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        headers.add("SOAPAction", "http://tempuri.org/" + soapAction);

        HttpEntity<String> entity = new HttpEntity<>(soapEnvelope, headers);

        try {
            log.debug("[BKAV-SOAP] Request to {}: {}", url, soapAction);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return extractResultFromSoapResponse(response.getBody(), soapAction);
            }
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "BKAV API trả về lỗi HTTP: " + response.getStatusCode());
        } catch (Exception e) {
            log.error("[BKAV-SOAP] Connection error: {}", e.getMessage());
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Không thể kết nối tới máy chủ BKAV: " + e.getMessage());
        }
    }

    private String extractResultFromSoapResponse(String soapResponse, String action) {
        String tagName = action + "Result";
        Pattern pattern = Pattern.compile("<" + tagName + ">(.*?)</" + tagName + ">", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(soapResponse);
        if (matcher.find()) {
            return unescapeXml(matcher.group(1));
        }
        return soapResponse;
    }

    private String unescapeXml(String text) {
        if (text == null) return "";
        return text.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&apos;", "'")
                .replace("&amp;", "&");
    }
}