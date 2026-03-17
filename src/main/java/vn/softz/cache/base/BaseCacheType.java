package vn.softz.cache.base;

public enum BaseCacheType {
    Memory(0),
    Redis(1),
    RedisCentinel(2);

    BaseCacheType(int value) {
    }
}
