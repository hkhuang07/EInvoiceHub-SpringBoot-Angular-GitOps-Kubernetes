package vn.softz.app.einvoicehub.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.service.EinvoiceMappingService.MappingType;
import vn.softz.app.einvoicehub.service.mapping.MappingStrategy;
import vn.softz.app.einvoicehub.service.mapping.MappingStrategyRegistry;
import vn.softz.cache.redis.RedisCache;
import vn.softz.cache.redis.RedisConfig;
import vn.softz.cache.redis.RedisDbIndex;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class EinvoiceMappingCache extends RedisCache<EinvoiceMappingCacheData> {

    private final MappingStrategyRegistry strategyRegistry;

    public EinvoiceMappingCache(
            MappingStrategyRegistry strategyRegistry,
            RedisConfig redisConfig) {
        super(
            RedisDbIndex.APP_SETTING.getValue(),
            EinvoiceMappingCacheData.class,
            "EinvMapping",
            redisConfig,
            Duration.ofHours(1)
        );
        this.strategyRegistry = strategyRegistry;
    }

    @Override
    @Transactional(readOnly = true)
    protected CompletableFuture<Optional<EinvoiceMappingCacheData>> loadFromDBAsync(
            String lid,
            String cacheKey) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                String[] parts = cacheKey.split(":");
                if (parts.length != 2) {
                    log.warn("Invalid cache key format: {}", cacheKey);
                    return Optional.empty();
                }

                String providerId = parts[0];
                String typeStr = parts[1];

                MappingType mappingType = MappingType.valueOf(typeStr);
                MappingStrategy strategy = strategyRegistry.getStrategy(mappingType);
                Map<String, String> mapping = strategy.loadMappings(lid, providerId);

                if (mapping.isEmpty()) {
                    log.debug("No mapping data found for: {}", cacheKey);
                    return Optional.empty();
                }

                log.info("Loaded {} mappings for: {} using {}",
                    mapping.size(),
                    cacheKey,
                    strategy.getClass().getSimpleName());

                return Optional.of(EinvoiceMappingCacheData.builder()
                    .cacheKey(cacheKey)
                    .mapping(mapping)
                    .build());

            } catch (Exception ex) {
                log.error("Failed to load mapping for: {}", cacheKey, ex);
                return Optional.empty();
            }
        });
    }
}
