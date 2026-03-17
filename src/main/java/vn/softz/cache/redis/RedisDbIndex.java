package vn.softz.cache.redis;

import lombok.Getter;

@Getter
public enum RedisDbIndex {
    DEFAULT(0),
    PROFILE(1),
    TRANSLATION(2),
    APP_SETTING(3),
    LastIndex(4);

    private final int value;

    RedisDbIndex(int value) {
        this.value = value;
    }

}
