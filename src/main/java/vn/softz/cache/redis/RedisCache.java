package vn.softz.cache.redis;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import vn.softz.cache.base.BaseCache;
import vn.softz.cache.base.BaseCacheData;
import vn.softz.cache.base.ICacheInstance;

import java.time.Duration;

public abstract class RedisCache<T extends BaseCacheData> extends BaseCache<T> {
    protected final RedisTemplate<String, Object> redisTemplate;
    protected final RedisProperties redisProperties;

    public RedisCache(Class<T> type, String prefix, RedisTemplate<String, Object> redisTemplate, Duration duration, boolean isLog, RedisProperties redisProperties) {
        super(type, prefix, duration, isLog, false);
        this.redisTemplate = redisTemplate;
        this.redisProperties = redisProperties;
        this.cacheInstance = createCacheInstance();
    }

    public RedisCache(Class<T> type, RedisTemplate<String, Object> redisTemplate, Duration duration, boolean isLog, RedisProperties redisProperties) {
        super(type, duration, isLog, false);
        this.redisTemplate = redisTemplate;
        this.redisProperties = redisProperties;
        this.cacheInstance = createCacheInstance();
    }

    public RedisCache(Class<T> type, RedisTemplate<String, Object> redisTemplate, RedisProperties redisProperties) {
        this(type, redisTemplate, Duration.ofMinutes(60), false, redisProperties);
    }

    public RedisCache(Class<T> type, String prefix, RedisTemplate<String, Object> redisTemplate, RedisProperties redisProperties) {
        this(type, prefix, redisTemplate, Duration.ofMinutes(60), false, redisProperties);
    }

    public RedisCache(int dbIndex, Class<T> type, String prefix, RedisConfig redisConfig, Duration duration) {
        super(type, prefix, duration, false, false);
        LettuceConnectionFactory lettuceConnectionFactory = redisConfig.createConnectionFactory(dbIndex);
        lettuceConnectionFactory.start();
        redisTemplate = redisConfig.createRedisTemplate(lettuceConnectionFactory);
        redisProperties = redisConfig.getRedisProperties();
        cacheInstance = createCacheInstance();
    }

    @Override
    protected ICacheInstance<T> createCacheInstance() {
        if (redisProperties == null || redisTemplate == null) {
            return null;
        }

        return new RedisCacheInstance<>(this.redisProperties, this.redisTemplate, getDuration(), getPrefix(), getType());
    }
}
