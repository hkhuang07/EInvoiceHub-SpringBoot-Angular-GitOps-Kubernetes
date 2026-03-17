package vn.softz.cache.base;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class BaseCache<T extends BaseCacheData> implements ICache<T> {
    protected static final String SEPARATOR_KEY = "@";
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected ICacheInstance<T> cacheInstance;
    protected final boolean isMemoryCache;
    protected final String prefix;
    private Duration duration;
    private boolean isLog;
    private Class<T> type;

    @SuppressWarnings("unchecked")
    public BaseCache() {
        isMemoryCache = true;
        prefix = type.getSimpleName();
        type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public BaseCache(Class<T> type, Duration duration, boolean isLog, boolean isMemoryCache) {
        this.type = type;
        this.prefix = type.getSimpleName();
        this.duration = duration;
        this.isLog = isLog;
        this.isMemoryCache = isMemoryCache;
        this.cacheInstance = initializeCacheInstance();
    }

    public BaseCache(Class<T> type, String prefix, Duration duration, boolean isLog, boolean isMemoryCache) {
        this.type = type;
        this.prefix = prefix == null ? type.getSimpleName() : prefix;
        this.duration = duration;
        this.isLog = isLog;
        this.isMemoryCache = isMemoryCache;
        this.cacheInstance = initializeCacheInstance();
    }

    public BaseCache(Class<T> type) {
        this(type, Duration.ofMinutes(60), false, false);
    }

    public BaseCache(Class<T> type, Duration duration, boolean isLog) {
        this(type, duration, isLog, true);
    }

    private ICacheInstance<T> initializeCacheInstance() {
        return createCacheInstance();
    }

    protected abstract ICacheInstance<T> createCacheInstance();

    @Override
    public CompletableFuture<Boolean> addAsync(String lid, T cacheObject) {
        return this.addAsync(lid, cacheObject, null);
    }

    @Override
    public CompletableFuture<Boolean> addAsync(String lid, T cacheObject, Duration expires) {
        return cacheInstance.addImplAsync(lid, cacheObject, expires);
    }

    @Override
    public CompletableFuture<Optional<T>> getAsync(String lid, String key) {
        if (key == null || key.isEmpty() || "CacheName".equals(key) || key.startsWith(":") || key.endsWith(":")) {
            return CompletableFuture.completedFuture(Optional.empty());
        }

        return cacheInstance.getImplAsync(lid, key)
                .thenCompose(instance -> {
                    if (instance.isEmpty()) {
                        return loadFromDBAsync(lid, key)
                                .thenCompose(dbInstance -> {
                                    if (dbInstance.isPresent()) {
                                        return addAsync(lid, dbInstance.get()).thenApply(v -> dbInstance);
                                    }
                                    return CompletableFuture.completedFuture(Optional.empty());
                                });
                    }
                    return CompletableFuture.completedFuture(instance);
                });
    }

    @Override
    public Optional<T> getValueData(String lid, String key) {
        return getAsync(lid, key).join();
    }

    @Override
    public CompletableFuture<List<T>> getAsync(String lid, String[] keys) {
        List<CompletableFuture<T>> futures = Arrays.stream(keys)
                .map(key -> getAsync(lid, key).thenApply(optional -> optional.orElse(null)))
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join) // This will get the T directly, or null if not present
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean isCacheManagement() {
        return true;
    }

    @Override
    public boolean isMemoryCache() {
        return this.isMemoryCache;
    }

    @Override
    public CompletableFuture<Void> reloadAsync(String lid, String key) {
        return removeAsync(lid, key)
                .thenCompose(v -> loadFromDBAsync(lid, key))
                .thenCompose(optionalValue -> {
                    if (optionalValue.isPresent()) {
                        // thenApply(added -> null) được dùng để chuyển CompletableFuture<Boolean> thành CompletableFuture<Void>
                        return addAsync(lid, optionalValue.get()).thenApply(added -> null);
                    }
                    return CompletableFuture.completedFuture(null);
                });
    }

    @Override
    public void reloadAsync(String lid) {
        cacheInstance.clearAsync(lid)
                .thenCompose(v -> loadFromDBAsync(lid))
                .thenCompose(values -> CompletableFuture.allOf(values.stream()
                        .map(value -> addAsync(lid, value)).toArray(CompletableFuture[]::new)));
    }

    @Override
    public CompletableFuture<Boolean> removeAsync(String lid, String key) {
        return cacheInstance.getImplAsync(lid, key)
                .thenCompose(cacheObject -> {
                    if (cacheObject.isPresent()) {
                        return cacheInstance.removeImplAsync(lid, key);
                    }
                    return CompletableFuture.completedFuture(false);
                });
    }

    protected CompletableFuture<Optional<T>> loadFromDBAsync(String lid, String key) {
        return CompletableFuture.completedFuture(Optional.empty());
    }

    protected CompletableFuture<List<T>> loadFromDBAsync(String lid) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    @Override
    public CompletableFuture<Long> countAsync() { return cacheInstance.countAsync(); }
    @Override
    public CompletableFuture<Map<String, T>> getAllAsync(String lid) { return cacheInstance.getAllAsync(lid); }
    @Override
    public CompletableFuture<Void> clearAsync(String lid) { return cacheInstance.clearAsync(lid); }
    @Override
    public CompletableFuture<List<T>> findAsync(String lid, String keyPattern) { return cacheInstance.findAsync(lid, keyPattern); }
    @Override
    public Set<String> getKeysByPattern(String pattern) { return cacheInstance.getKeysByPattern(pattern); }
}