package vn.softz.cache.base;

import lombok.EqualsAndHashCode;

import java.time.Instant;

public abstract class BaseCacheData {
    public abstract String getKey();
    public Instant cacheUpdateAt = Instant.now();
}
