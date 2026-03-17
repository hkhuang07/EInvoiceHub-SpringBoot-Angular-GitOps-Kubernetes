package vn.softz.cache.memory;

import vn.softz.cache.base.BaseCache;
import vn.softz.cache.base.BaseCacheData;
import vn.softz.cache.base.ICacheInstance;

import java.time.Duration;

public class InMemoryCache<T extends BaseCacheData> extends BaseCache<T> {
    protected InMemoryCache(Class<T> type, Duration duration, boolean isLog) {
        super(type, duration, isLog);
        reloadAsync("Initial load for " + type.getSimpleName());
    }

    protected InMemoryCache(Class<T> type) {
        super(type, Duration.ofMinutes(60), false);
        reloadAsync("Initial load for " + type.getSimpleName());
    }

    @Override
    protected ICacheInstance<T> createCacheInstance() {
        return new MemoryCacheInstance<T>(getDuration());
    }
}
