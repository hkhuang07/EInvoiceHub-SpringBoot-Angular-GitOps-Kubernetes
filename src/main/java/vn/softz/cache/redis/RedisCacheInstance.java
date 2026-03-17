package vn.softz.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import vn.softz.cache.base.BaseCacheData;
import vn.softz.cache.base.ICacheInstance;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
class RedisCacheInstance<T extends BaseCacheData> implements ICacheInstance<T> {
    private final RedisProperties redisProperties;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Duration defaultExpire;
    private final String keyPrefix;
    private final Class<T> type;
    private final ObjectMapper objectMapper;

    public RedisCacheInstance(RedisProperties redisProperties, RedisTemplate<String, Object> redisTemplate, Duration duration, String prefix, Class<T> type) {
        this.redisProperties = redisProperties;
        this.redisTemplate = redisTemplate;
        this.defaultExpire = duration;
        this.keyPrefix = (prefix == null || prefix.isBlank()) ? "" : prefix + ":";
        this.type = type;
        this.objectMapper = JsonMapper.builder()
                .build();
    }

    private String buildKey(String key) {
        return redisProperties.getPrefix() + keyPrefix + key;
    }

    @Override
    public CompletableFuture<Long> countAsync() {
        return CompletableFuture.supplyAsync(() -> {
            assert redisTemplate.getConnectionFactory() != null;
            try (var connection = redisTemplate.getConnectionFactory().getConnection()) {
                return connection.commands().dbSize();
            }
        });
    }

    @Override
    public CompletableFuture<Void> clearAsync(String lid) {
        log.warn("Executing 'clearAsync' which uses the 'KEYS' command. This is not recommended for production.");
        Set<String> keys = getKeysByPattern(buildKey("*"));
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public Set<String> getKeysByPattern(String pattern) {
        Set<String> keys = new HashSet<>();
        final String finalPattern = buildKey("*" + pattern + "*");
        ScanOptions scanOptions = ScanOptions.scanOptions().match(finalPattern).count(100).build();  // count: số lượng ước lượng mỗi lần scan

        assert redisTemplate.getConnectionFactory() != null;
        try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection()
                .keyCommands().scan(scanOptions)) {
            while (cursor.hasNext()) {
                keys.add(new String(cursor.next()));
            }
        }  // try-with-resources để tự động close cursor

        return keys;
    }

    @Override
    public CompletableFuture<List<T>> findAsync(String lid, String keyPattern) {
        log.warn("Executing 'findAsync' which uses the 'KEYS' command. This is not recommended for production.");
        return CompletableFuture.supplyAsync(() -> {
            Set<String> keys = getKeysByPattern(keyPattern);
            if (keys.isEmpty()) {
                return Collections.emptyList();
            }
            List<Object> values = redisTemplate.opsForValue().multiGet(keys);
            assert values != null;
            return values.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToInstance)
                    .filter(Objects::nonNull)
                    .filter(x -> x.getKey() != null)
                    .collect(Collectors.toList());
        });
    }

    @Override
    public CompletableFuture<Map<String, T>> getAllAsync(String lid) {
        log.warn("Executing 'getAllAsync' which uses the 'KEYS' command. This is not recommended for production.");
        return CompletableFuture.supplyAsync(() -> {
            Set<String> keys = getKeysByPattern("*");
            if (keys.isEmpty()) {
                return Collections.emptyMap();
            }
            List<Object> values = redisTemplate.opsForValue().multiGet(keys);
            assert values != null;
            return values.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToInstance)
                    .filter(Objects::nonNull)
                    .filter(x -> x.getKey() != null)
                    .collect(Collectors.toMap(BaseCacheData::getKey, v -> v, (v1, v2) -> v1));
        });
    }

    @Override
    public CompletableFuture<Boolean> addImplAsync(String lid, T cacheObject, Duration expires) {
        Duration effectiveExpire = (expires != null) ? expires : this.defaultExpire;
        String finalKey = buildKey(cacheObject.getKey());
        redisTemplate.opsForValue().set(finalKey, cacheObject, effectiveExpire);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Boolean> addImplAsync(String lid, T cacheObject) {
        return addImplAsync(lid, cacheObject, this.defaultExpire);
    }

    private T mapToInstance(Object value) {
        if (value instanceof BaseCacheData) {
            return (T) value;
        } else if (value instanceof Map) {
            try {
                return objectMapper.convertValue(value, type);
            } catch (IllegalArgumentException e) {
                log.error("Error converting Map to BaseCacheData", e);
                return null;
            }
        }
        return null;
    }

    @Override
    public CompletableFuture<Optional<T>> getImplAsync(String lid, String key) {
        return CompletableFuture.supplyAsync(() -> {
            String finalKey = buildKey(key);
            Object value = redisTemplate.opsForValue().get(finalKey);
            var instance = mapToInstance(value);
            return instance != null ? Optional.of(instance) : Optional.empty();
        });
    }

    @Override
    public CompletableFuture<Boolean> removeImplAsync(String lid, String key) {
        String finalKey = buildKey(key);
        Boolean result = redisTemplate.delete(finalKey);
        return CompletableFuture.completedFuture(result);
    }
}
