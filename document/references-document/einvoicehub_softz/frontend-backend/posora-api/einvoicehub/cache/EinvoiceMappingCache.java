package vn.softz.app.einvoicehub.cache;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.repository.mapping.EinvMappingInvoiceStatusRepository;
import vn.softz.app.einvoicehub.domain.repository.mapping.EinvMappingInvoiceTypeRepository;
import vn.softz.app.einvoicehub.domain.repository.mapping.EinvMappingItemTypeRepository;
import vn.softz.app.einvoicehub.domain.repository.mapping.EinvMappingPaymentMethodRepository;
import vn.softz.app.einvoicehub.domain.repository.mapping.EinvMappingReferenceTypeRepository;
import vn.softz.app.einvoicehub.domain.repository.mapping.EinvMappingTaxTypeRepository;
import vn.softz.cache.redis.RedisCache;
import vn.softz.cache.redis.RedisDbIndex;
import vn.softz.cache.redis.RedisConfig;

@Component
public class EinvoiceMappingCache extends RedisCache<EinvoiceMappingCacheData> {
    private final EinvMappingTaxTypeRepository taxTypeRepository;
    private final EinvMappingInvoiceStatusRepository statusRepository;
    private final EinvMappingInvoiceTypeRepository typeRepository;
    private final EinvMappingPaymentMethodRepository paymentRepository;
    private final EinvMappingItemTypeRepository itemRepository;
    private final EinvMappingReferenceTypeRepository referenceRepository;

    // constructors
    @Autowired
    public EinvoiceMappingCache(
        EinvMappingTaxTypeRepository taxTypeRepository,
        EinvMappingInvoiceStatusRepository statusRepository,
        EinvMappingInvoiceTypeRepository typeRepository,
        EinvMappingPaymentMethodRepository paymentRepository,
        EinvMappingItemTypeRepository itemRepository,
        EinvMappingReferenceTypeRepository referenceRepository,
        RedisConfig redisConfig) {

            super(RedisDbIndex.APP_SETTING.getValue(), 
              EinvoiceMappingCacheData.class, // kiểu dữ liệu
              "EinvMapping",  // prefix cho key
              redisConfig, 
              Duration.ofHours(1));
        
            this.taxTypeRepository = taxTypeRepository;
            this.statusRepository = statusRepository;
            this.typeRepository = typeRepository;
            this.paymentRepository = paymentRepository;
            this.itemRepository = itemRepository;
            this.referenceRepository = referenceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    protected CompletableFuture<Optional<EinvoiceMappingCacheData>> loadFromDBAsync(String lid, String cacheKey) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String[] parts = cacheKey.split(":");
                if (parts.length != 2) {
                    log.warn("Invalid cache key format: {}", cacheKey);
                    return Optional.empty();
                }
                String providerId = parts[0];
                String type = parts[1].toUpperCase();
                Map<String, String> mapping = new HashMap<>();
                switch (type) {
                    case "TAX_TYPE":
                        taxTypeRepository.findAllByProviderIdAndInactiveFalse(providerId)
                            .forEach(e -> mapping.put(e.getTaxTypeId(), 
                                e.getProviderTaxTypeId() + "," + e.getProviderTaxRate()));
                        break;
                        
                    case "INVOICE_STATUS":
                        statusRepository.findAllByProviderIdAndInactiveFalse(providerId)
                            .forEach(e -> mapping.put(String.valueOf(e.getInvoiceStatusId()), 
                                                     e.getProviderInvoiceStatusId()));
                        break;
                        
                    case "INVOICE_TYPE":
                        typeRepository.findAllByProviderIdAndInactiveFalse(providerId)
                            .forEach(e -> mapping.put(String.valueOf(e.getInvoiceTypeId()), 
                                                     e.getProviderInvoiceTypeId()));
                        break;
                        
                    case "ITEM_TYPE":
                        log.debug("[CACHE_LOAD] Loading ITEM_TYPE mapping for provider: {}", providerId);
                        itemRepository.findAllByProviderIdAndInactiveFalse(providerId)
                            .forEach(e -> {
                                mapping.put(String.valueOf(e.getItemTypeId()), e.getProviderItemTypeId());
                                log.debug("[CACHE_LOAD] ITEM_TYPE: {} → '{}'", e.getItemTypeId(), e.getProviderItemTypeId());
                            });
                        log.info("[CACHE_LOAD] Loaded {} ITEM_TYPE mappings for {}", mapping.size(), providerId);
                        break;
                        
                    case "PAYMENT_METHOD":
                        log.debug("[CACHE_LOAD] Loading PAYMENT_METHOD mapping for provider: {}", providerId);
                        paymentRepository.findAllByProviderIdAndInactiveFalse(providerId)
                            .forEach(e -> {
                                mapping.put(String.valueOf(e.getPaymentMethodId()), e.getProviderPaymentMethodId());
                                log.debug("[CACHE_LOAD] PAYMENT_METHOD: {} → '{}'", e.getPaymentMethodId(), e.getProviderPaymentMethodId());
                            });
                        log.info("[CACHE_LOAD] Loaded {} PAYMENT_METHOD mappings for {}", mapping.size(), providerId);
                        break;
                        
                    case "REFERENCE_TYPE":
                        referenceRepository.findAllByProviderIdAndInactiveFalse(providerId)
                            .forEach(e -> mapping.put(String.valueOf(e.getReferenceTypeId()), 
                                                     e.getProviderReferenceTypeId()));
                        break;
                        
                    default:
                        log.warn("Unknown mapping type: {}", type);
                        return Optional.empty();
                }
                if (mapping.isEmpty()) {
                    log.debug("No mapping data found for: {}", cacheKey);
                    return Optional.empty();
                }
                EinvoiceMappingCacheData cacheData = EinvoiceMappingCacheData.builder()
                    .cacheKey(cacheKey)
                    .mapping(mapping)
                    .build();
                log.debug("Loaded {} mappings for key: {}", mapping.size(), cacheKey);
                return Optional.of(cacheData);
            } catch (Exception ex) {
                log.error("Failed to load mapping for key: {}", cacheKey, ex);
                return Optional.empty();
            }
        });
    }
}
