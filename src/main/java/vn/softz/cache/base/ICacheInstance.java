package vn.softz.cache.base;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for a cache instance, converted from C#.
 * Handles asynchronous cache operations.
 *
 * @param <T> The type of the data stored in the cache, must extend BaseCacheData.
 */
public interface ICacheInstance<T extends BaseCacheData> {

    CompletableFuture<Long> countAsync();

    CompletableFuture<Void> clearAsync(String lid);

    CompletableFuture<List<T>> findAsync(String lid, String keyPattern);
    Set<String> getKeysByPattern(String pattern);

    CompletableFuture<Map<String, T>> getAllAsync(String lid);

    CompletableFuture<Boolean> addImplAsync(String lid, T cacheObject, Duration expires);
    CompletableFuture<Boolean> addImplAsync(String lid, T cacheObject);

    CompletableFuture<Optional<T>> getImplAsync(String lid, String key);

    CompletableFuture<Boolean> removeImplAsync(String lid, String key);
}