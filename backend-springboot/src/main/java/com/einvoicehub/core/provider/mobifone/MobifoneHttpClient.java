package vn.softz.app.einvoicehub.provider.mobifone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import vn.softz.app.einvoicehub.domain.entity.EinvProviderEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvProviderRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderRepository;
import vn.softz.app.einvoicehub.provider.mobifone.constant.MobifoneApiEndpoints;
import vn.softz.app.einvoicehub.provider.mobifone.model.MobifoneLoginRequest;
import vn.softz.app.einvoicehub.provider.mobifone.model.MobifoneLoginResponse;
import vn.softz.core.common.Common;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MobifoneHttpClient {
    
    private static final long TOKEN_VALIDITY_SECONDS = 3600;
    private static final String MOBI_PROVIDER_ID = "MOBI";
    private static final String DEFAULT_BASE_URL = "http://mobiinvoice.vn:9000";
    
    private final EinvStoreProviderRepository storeProviderRepository;
    private final EinvProviderRepository providerRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    private String cachedToken;
    private String cachedMaDvcs;
    private Instant tokenExpiry;
    
    public MobifoneHttpClient(EinvStoreProviderRepository storeProviderRepository, EinvProviderRepository providerRepository) {
        this.storeProviderRepository = storeProviderRepository;
        this.providerRepository = providerRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private String getBaseUrl() {
        return providerRepository.findById(MOBI_PROVIDER_ID)
                .map(EinvProviderEntity::getIntegrationUrl)
                .orElse(DEFAULT_BASE_URL);
    }

    private EinvStoreProviderEntity getConfig() {
        String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
        return storeProviderRepository.findByStoreId(storeId)
                .orElseThrow(() -> new RuntimeException("Chưa cấu hình HĐĐT cho cửa hàng này"));
    }
    
    public MobifoneLoginResponse login() {
        try {
            var config = getConfig();
            String url = getBaseUrl() + MobifoneApiEndpoints.LOGIN;
            
            MobifoneLoginRequest request = MobifoneLoginRequest.builder()
                    .username(config.getPartnerUsr())
                    .password(config.getPartnerPwd())
                    .taxCode(config.getTaxCode())
                    .build();
            
            HttpEntity<MobifoneLoginRequest> entity = new HttpEntity<>(request, createJsonHeaders());
            
            log.info("MobiFone login - URL: {}, TaxCode: {}", url, config.getTaxCode());
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            MobifoneLoginResponse loginResponse = objectMapper.readValue(response.getBody(), MobifoneLoginResponse.class);
            
            this.cachedToken = loginResponse.getToken();
            this.cachedMaDvcs = loginResponse.getMaDvcs();
            this.tokenExpiry = Instant.now().plusSeconds(TOKEN_VALIDITY_SECONDS);
            
            log.info("MobiFone login successful - ma_dvcs: {}", loginResponse.getMaDvcs());
            return loginResponse;
            
        } catch (HttpStatusCodeException e) {
            log.error("MobiFone login failed - Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Login thất bại: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("MobiFone login error", e);
            throw new RuntimeException("Lỗi kết nối MobiFone: " + e.getMessage());
        }
    }

    public MobifoneLoginResponse loginWithCredentials(String username, String password, String taxCode) {
        try {
            String url = getBaseUrl() + MobifoneApiEndpoints.LOGIN;
            
            MobifoneLoginRequest request = MobifoneLoginRequest.builder()
                    .username(username)
                    .password(password)
                    .taxCode(taxCode)
                    .build();
            
            HttpEntity<MobifoneLoginRequest> entity = new HttpEntity<>(request, createJsonHeaders());
            
            log.info("MobiFone loginWithCredentials - URL: {}, Username: {}", url, username);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return objectMapper.readValue(response.getBody(), MobifoneLoginResponse.class);
            
        } catch (HttpStatusCodeException e) {
            log.error("MobiFone login failed - Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Login thất bại: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("MobiFone login error", e);
            throw new RuntimeException("Lỗi kết nối MobiFone: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public Object loginRaw() {
        try {
            var config = getConfig();
            String url = getBaseUrl() + MobifoneApiEndpoints.LOGIN;
            
            MobifoneLoginRequest request = MobifoneLoginRequest.builder()
                    .username(config.getPartnerUsr())
                    .password(config.getPartnerPwd())
                    .taxCode(config.getTaxCode())
                    .build();
            
            HttpEntity<MobifoneLoginRequest> entity = new HttpEntity<>(request, createJsonHeaders());
            
            log.info("MobiFone loginRaw - URL: {}, TaxCode: {}", url, config.getTaxCode());
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            Object rawResponse = objectMapper.readValue(response.getBody(), Object.class);
            
            if (rawResponse instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) rawResponse;
                if (map.containsKey("token")) {
                    this.cachedToken = (String) map.get("token");
                    this.cachedMaDvcs = (String) map.get("ma_dvcs");
                    this.tokenExpiry = Instant.now().plusSeconds(TOKEN_VALIDITY_SECONDS);
                    log.info("MobiFone loginRaw successful - ma_dvcs: {}", this.cachedMaDvcs);
                }
            }
            
            return rawResponse;
            
        } catch (HttpStatusCodeException e) {
            log.error("MobiFone loginRaw failed - Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Login thất bại: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("MobiFone loginRaw error", e);
            throw new RuntimeException("Lỗi kết nối MobiFone: " + e.getMessage());
        }
    }
    
    public String getToken() {
        if (cachedToken == null || tokenExpiry == null || Instant.now().isAfter(tokenExpiry)) {
            log.debug("Token expired or not available, refreshing...");
            login();
        }
        return cachedToken;
    }
    
    public String getMaDvcs() {
        if (cachedMaDvcs == null) {
            login();
        }
        return cachedMaDvcs;
    }
    
    public <T> T post(String endpoint, Object requestBody, Class<T> responseType) {
        return executeWithRetry(() -> {
            String url = getBaseUrl() + endpoint;
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, buildAuthHeaders());
            
            log.debug("MobiFone POST - URL: {}", url);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            log.debug("MobiFone Response: {}", response.getBody());
            
            return objectMapper.readValue(response.getBody(), responseType);
        });
    }
    
    public <T> List<T> postForList(String endpoint, Object requestBody, TypeReference<List<T>> typeRef) {
        return executeWithRetry(() -> {
            String url = getBaseUrl() + endpoint;
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, buildAuthHeaders());
            
            log.debug("MobiFone POST - URL: {}", url);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            log.debug("MobiFone Response: {}", response.getBody());
            
            return objectMapper.readValue(response.getBody(), typeRef);
        });
    }
    
    public <T> T get(String endpoint, Class<T> responseType) {
        return executeWithRetry(() -> {
            String url = buildUrlWithTaxCode(getBaseUrl() + endpoint);
            HttpEntity<Void> entity = new HttpEntity<>(buildAuthHeaders());
            
            log.debug("MobiFone GET - URL: {}", url);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            log.debug("MobiFone Response: {}", response.getBody());
            
            return objectMapper.readValue(response.getBody(), responseType);
        });
    }
    
    public byte[] getBytes(String endpoint) {
        return executeWithRetry(() -> {
            String url = buildUrlWithTaxCode(getBaseUrl() + endpoint);
            
            HttpHeaders headers = buildAuthHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_PDF));
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            log.debug("MobiFone GET bytes - URL: {}", url);
            
            ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
            return response.getBody();
        });
    }
    
    public String getString(String endpoint) {
        return executeWithRetry(() -> {
            String url = buildUrlWithTaxCode(getBaseUrl() + endpoint);
            HttpEntity<Void> entity = new HttpEntity<>(buildAuthHeaders());
            
            log.debug("MobiFone GET string - URL: {}", url);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        });
    }
    
    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
    
    private HttpHeaders buildAuthHeaders() {
        HttpHeaders headers = createJsonHeaders();
        headers.set("Authorization", "Bearer " + getToken() + ";" + getMaDvcs());
        return headers;
    }

    public Object getInvoiceSeries() {
        log.info("Getting MobiFone invoice series");
        return get(MobifoneApiEndpoints.GET_DATA_REFERENCES + "?refId=RF00059", Object.class);
    }
    
    private String buildUrlWithTaxCode(String url) {
        String separator = url.contains("?") ? "&" : "?";
        return url + separator + "tax_code=" + getConfig().getTaxCode();
    }
    
    private <T> T executeWithRetry(SupplierWithException<T> action) {
        try {
            return action.get();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.warn("MobiFone token expired, refreshing and retrying...");
                this.cachedToken = null;
                this.tokenExpiry = null;
                try {
                    return action.get();
                } catch (Exception retryEx) {
                    log.error("MobiFone retry failed", retryEx);
                    throw new RuntimeException("Lỗi MobiFone sau khi retry: " + retryEx.getMessage());
                }
            }
            log.error("MobiFone HTTP error - Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Lỗi MobiFone: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("MobiFone error", e);
            throw new RuntimeException("Lỗi kết nối MobiFone: " + e.getMessage());
        }
    }
    
    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get() throws Exception;
    }
}
