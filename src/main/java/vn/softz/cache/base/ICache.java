package vn.softz.cache.base;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

interface ICache<T extends BaseCacheData> {
    CompletableFuture<Boolean> addAsync(String lid, T cacheObject);
    CompletableFuture<Boolean> addAsync(String lid, T cacheObject, Duration expires);
    Optional<T> getValueData(String lid, String key);
    CompletableFuture<Optional<T>> getAsync(String lid, String key);
    CompletableFuture<List<T>> getAsync(String lid, String[] keys);
    CompletableFuture<Boolean> removeAsync(String lid, String key);
    CompletableFuture<Void> reloadAsync(String lid, String key);
    void reloadAsync(String lid);
    CompletableFuture<Long> countAsync();
    CompletableFuture<Map<String, T>> getAllAsync(String lid);
    CompletableFuture<Void> clearAsync(String lid);
    CompletableFuture<List<T>> findAsync(String lid, String keyPattern);
    Set<String> getKeysByPattern(String pattern);
    boolean isCacheManagement();
    boolean isMemoryCache();
}