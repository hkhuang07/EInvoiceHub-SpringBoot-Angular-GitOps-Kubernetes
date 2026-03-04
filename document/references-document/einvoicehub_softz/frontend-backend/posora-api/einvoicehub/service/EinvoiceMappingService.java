package vn.softz.app.einvoicehub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.softz.app.einvoicehub.cache.EinvoiceMappingCache;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvoiceMappingService {
    private final EinvoiceMappingCache mappingCache;
    public enum MappingType {
        TAX_TYPE,
        INVOICE_STATUS,
        INVOICE_TYPE,
        ITEM_TYPE,
        PAYMENT_METHOD,
        REFERENCE_TYPE
    }

    public CompletableFuture<String> getProviderCodeAsync(String lid, String providerId, MappingType type, String internalCode) {
        if (internalCode == null || internalCode.isEmpty()) {
            log.debug("[MAPPING_SERVICE] {} - Internal code is null/empty", type);
            return CompletableFuture.completedFuture(null);
        }

        String cacheKey = providerId + ":" + type.name();
        log.debug("[MAPPING_SERVICE] {} - Looking up: {} (cacheKey: {})", type, internalCode, cacheKey);

        return mappingCache.getAsync(lid, cacheKey)
            .thenApply(optionalData -> {
                if (optionalData.isEmpty()) {
                    log.warn("[MAPPING_SERVICE] {} - NO CACHE DATA for key: {}, fallback to: {}", 
                        type, cacheKey, internalCode);
                    return internalCode;
                }
                
                Map<String, String> mapping = optionalData.get().getMapping();
                log.debug("[MAPPING_SERVICE] {} - Cache hit! Mapping size: {}", type, mapping.size());
                
                String providerCode = mapping.get(internalCode);

                if (providerCode == null) {
                    log.warn("[MAPPING_SERVICE] {} - Code '{}' NOT FOUND in mapping (available: {}), using original", 
                        type, internalCode, mapping.keySet());
                    return internalCode;
                }

                log.info("[MAPPING_SERVICE] {} - SUCCESS: {} → '{}'", type, internalCode, providerCode);
                return providerCode;
            })
            .exceptionally(ex -> {
                log.error("[MAPPING_SERVICE] {} - ERROR for {}: {}", type, cacheKey, ex.getMessage(), ex);
                return internalCode;
            });
    }

    public String getProviderCode(String lid, String providerId, MappingType type, String internalCode) {
        return getProviderCodeAsync(lid, providerId, type, internalCode).join();
    }

    public CompletableFuture<String> getInternalCodeAsync(String lid, String providerId, MappingType type, String providerCode) {
        if (providerCode == null || providerCode.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        String cacheKey = providerId + ":" + type.name();

        return mappingCache.getAsync(lid, cacheKey)
            .thenApply(optionalData -> {
                if (optionalData.isEmpty()) {
                    log.debug("No mapping data found for: {}", cacheKey);
                    return providerCode;
                }

                Map<String, String> mapping = optionalData.get().getMapping();

                Optional<String> internalCode = mapping.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(providerCode))
                    .map(Map.Entry::getKey)
                    .findFirst();
                
                if (internalCode.isEmpty()) {
                    log.debug("Mapping not found for {} in {}, using original value", providerCode, cacheKey);
                    return providerCode;
                }

                return internalCode.get();
            })
            .exceptionally(ex -> {
                log.debug("Error getting internal code for {}: {}", cacheKey, ex.getMessage());
                return providerCode;
            });
    }

    public String getInternalCode(String lid, String providerId, MappingType type, String providerCode) {
        return getInternalCodeAsync(lid, providerId, type, providerCode).join();
    }
    
    public CompletableFuture<Boolean> evictAsync(String lid, String providerId, MappingType type) {
        String cacheKey = providerId + ":" + type.name();
        log.info("Evicting cache for: {}", cacheKey);
        return mappingCache.removeAsync(lid, cacheKey);
    }
    
    public CompletableFuture<Void> reloadAsync(String lid, String providerId, MappingType type) {
        String cacheKey = providerId + ":" + type.name();
        log.info("Reloading cache for: {}", cacheKey);
        return mappingCache.reloadAsync(lid, cacheKey);
    }
}
