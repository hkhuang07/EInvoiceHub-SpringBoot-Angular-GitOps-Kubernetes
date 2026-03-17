package vn.softz.app.einvoicehub.provider.bkav;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import vn.softz.app.einvoicehub.domain.repository.EinvProviderRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderRepository;
import vn.softz.app.einvoicehub.provider.bkav.model.BkavResponse;
import vn.softz.app.einvoicehub.provider.bkav.util.BkavCryptoUtil;
import vn.softz.core.common.Common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
@RequiredArgsConstructor
public class BkavSoapClient {

    private static final String BKAV_PROVIDER_ID = "BKAV";
    private static final String DEFAULT_ENDPOINT = "https://wsdemo.ehoadon.vn/WSPublicEHoaDon.asmx";
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private final EinvProviderRepository providerRepository;
    private final EinvStoreProviderRepository storeProviderRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    // Gửi lệnh lên BKAV SOAP API.
    // commandObject có thể là String (guid, taxCode...) hoặc Object (List<BkavInvoice.RequestData>...)
    // SoapClient tự serialize Object → JSON string trước khi encrypt.
    public BkavResponse executeCommand(String storeId, int commandType, Object commandObject) {
        try {
            var config = storeProviderRepository.findByStoreId(storeId)
                    .orElseThrow(() -> new RuntimeException("Chưa cấu hình HĐĐT cho cửa hàng này"));

            String partnerGuid = config.getPartnerId();
            String partnerToken = config.getPartnerToken();

            if (partnerGuid == null || partnerToken == null) {
                return createErrorResponse(-1, "Thiếu thông tin PartnerGUID hoặc PartnerToken");
            }

            String endpoint = providerRepository.findById(BKAV_PROVIDER_ID)
                    .map(p -> p.getIntegrationUrl())
                    .orElse(DEFAULT_ENDPOINT);

            // Serialize commandObject → JSON string nếu chưa phải String
            String commandJson = toJsonString(commandObject);
            String soapRequest = buildSoapEnvelope(commandType, commandJson, partnerGuid, partnerToken);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_XML);
            headers.set("SOAPAction", "http://tempuri.org/ExecuteCommand");

            HttpEntity<String> request = new HttpEntity<>(soapRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            return parseSoapResponse(response.getBody(), partnerToken);

        } catch (Exception e) {
            log.error("Error calling BKAV SOAP service", e);
            return createErrorResponse(-1, "Lỗi kết nối BKAV: " + e.getMessage());
        }
    }

    // Serialize object → JSON string
    // Nếu đã là String thì giữ nguyên (uuid, taxCode, guid...)
    private String toJsonString(Object commandObject) throws Exception {
        if (commandObject instanceof String) {
            return (String) commandObject;
        }
        return OBJECT_MAPPER.writeValueAsString(commandObject);
    }

    private String buildSoapEnvelope(int commandType, String commandJson,
                                     String partnerGuid, String partnerToken) {
        try {
            // Nếu commandJson là plain string (uuid, taxCode...) thì wrap trong quotes
            String commandValue = commandJson.startsWith("{") || commandJson.startsWith("[")
                    ? commandJson
                    : "\"" + commandJson + "\"";

            String fullCommand = String.format(
                    "{\"CmdType\":%d,\"CommandObject\":%s}",
                    commandType,
                    commandValue
            );

            String encryptedData = BkavCryptoUtil.encryptData(fullCommand, partnerToken);

            return String.format(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                            "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                            "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                            "<soap:Body>" +
                            "<ExecuteCommand xmlns=\"http://tempuri.org/\">" +
                            "<PartnerGUID>%s</PartnerGUID>" +
                            "<EncryptedCommandData>%s</EncryptedCommandData>" +
                            "</ExecuteCommand>" +
                            "</soap:Body>" +
                            "</soap:Envelope>",
                    partnerGuid,
                    encryptedData
            );

        } catch (Exception e) {
            log.error("Error encrypting command data", e);
            throw new RuntimeException("Failed to encrypt command data: " + e.getMessage(), e);
        }
    }

    private BkavResponse parseSoapResponse(String soapXml, String partnerToken) {
        try {
            Pattern pattern = Pattern.compile("<ExecuteCommandResult>(.*?)</ExecuteCommandResult>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(soapXml);

            if (!matcher.find()) {
                log.error("Cannot find ExecuteCommandResult in SOAP response");
                return createErrorResponse(-1, "Invalid SOAP response");
            }

            String resultText = unescapeXml(matcher.group(1)).trim();
            log.debug("Extracted result: {}", resultText);

            // Nếu không phải JSON → response đang bị encrypt
            if (!resultText.startsWith("{") && !resultText.startsWith("[")) {
                log.debug("Detected encrypted response from BKAV, decrypting...");
                try {
                    String decryptedJson = BkavCryptoUtil.decryptData(resultText, partnerToken);
                    log.debug("Decrypted response: {}", decryptedJson);
                    BkavResponse result = OBJECT_MAPPER.readValue(decryptedJson, BkavResponse.class);
                    log.info("BKAV Response - Status: {}, isOk: {}, Message: {}",
                            result.getStatus(), result.getIsOk(), result.getMessage());
                    return result;
                } catch (Exception decryptEx) {
                    log.warn("Failed to decrypt response: {}", decryptEx.getMessage());
                    return createErrorResponse(-1, resultText);
                }
            }

            BkavResponse result = OBJECT_MAPPER.readValue(resultText, BkavResponse.class);
            log.info("BKAV Response - Code: {}, Message: {}", result.getCode(), result.getMessage());
            return result;

        } catch (Exception e) {
            log.error("Error parsing SOAP response", e);
            return createErrorResponse(-1, "Lỗi parse response: " + e.getMessage());
        }
    }

    private BkavResponse createErrorResponse(int code, String message) {
        BkavResponse response = new BkavResponse();
        response.setStatus(code);
        response.setCode(code);
        response.setMessage(message);
        response.setIsOk(false);
        response.setIsError(true);
        return response;
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