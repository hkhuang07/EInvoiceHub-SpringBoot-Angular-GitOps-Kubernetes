package vn.softz.cache.memory;

import com.github.benmanes.caffeine.cache.*;
import org.springframework.beans.factory.annotation.Value;
import vn.softz.cache.base.BaseCacheData;
import vn.softz.cache.base.ICacheInstance;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

class MemoryCacheInstance<T extends BaseCacheData> implements ICacheInstance<T> {
    @Value("spring.cache.caffeine.maximum-size:10000")
    private long maximumCacheSize;
    private final LoadingCache<String, T> cache;
    private final ConcurrentHashMap<String, Duration> customExpirations;
    private final Duration defaultExpire;
    public MemoryCacheInstance(Duration duration, com.github.benmanes.caffeine.cache.Ticker ticker) {
        this.defaultExpire = duration;
        this.customExpirations = new ConcurrentHashMap<>();
        this.cache = Caffeine.newBuilder()
                .maximumSize(maximumCacheSize > 0L ? maximumCacheSize : 10000)
                .ticker(ticker)
                .expireAfter(new Expiry<String, T>() {
                    @Override
                    public long expireAfterCreate(String key, T value, long currentTime) {
                        return getExpirationForKey(key).toNanos();
                    }

                    @Override
                    public long expireAfterUpdate(String key, T value, long currentTime, long currentDuration) {
                        return getExpirationForKey(key).toNanos();
                    }

                    @Override
                    public long expireAfterRead(String key, T value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                })
                .removalListener((key, value, cause) -> {
                    if (key != null) {
                        customExpirations.remove(key);
                    }
                })
                .build(key -> null);
    }

    public MemoryCacheInstance(Duration duration) {
        this(duration, Ticker.systemTicker());
    }
    public MemoryCacheInstance() {
        this(Duration.ofHours(1), Ticker.systemTicker());
    }

    private Duration getExpirationForKey(String key) {
        return customExpirations.getOrDefault(key, defaultExpire);
    }

    @Override
    public CompletableFuture<Long> countAsync() {
        return CompletableFuture.completedFuture(cache.estimatedSize());
    }

    @Override
    public CompletableFuture<Void> clearAsync(String lid) {
        cache.invalidateAll();
        customExpirations.clear();
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<List<T>> findAsync(String lid, String keyPattern) {
        return CompletableFuture.completedFuture(
            cache.asMap().values().stream()
                .filter(data -> data.getKey().contains(keyPattern))
                .collect(java.util.stream.Collectors.toList())
        );
    }

    @Override
    public Set<String> getKeysByPattern(String pattern) {
        return cache.asMap().values().stream()
                .map(BaseCacheData::getKey)
                .filter(key -> key.contains(pattern))
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public CompletableFuture<Map<String, T>> getAllAsync(String lid) {
        return CompletableFuture.completedFuture(cache.asMap());
    }

    @Override
    public CompletableFuture<Boolean> addImplAsync(String lid, T cacheObject, Duration expires) {
        if (expires != null) {
            customExpirations.put(cacheObject.getKey(), expires);
        }
        cache.put(cacheObject.getKey(), cacheObject);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Boolean> addImplAsync(String lid, T cacheObject) {
        cache.put(cacheObject.getKey(), cacheObject);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Optional<T>> getImplAsync(String lid, String key) {
        return CompletableFuture.completedFuture(Optional.ofNullable(cache.get(key)));
    }

    @Override
    public CompletableFuture<Boolean> removeImplAsync(String lid, String key) {
        cache.invalidate(key);
        customExpirations.remove(key);
        return CompletableFuture.completedFuture(true);
    }
}
