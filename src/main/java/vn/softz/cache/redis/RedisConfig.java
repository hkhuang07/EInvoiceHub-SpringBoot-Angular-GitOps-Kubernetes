package vn.softz.cache.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import vn.softz.common.utils.StringUtils;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Getter
    private final RedisProperties redisProperties;

    public LettuceConnectionFactory createConnectionFactory(int dbIndex) {
        if (redisProperties.getSentinel() != null && StringUtils.isNotBlank(redisProperties.getSentinel().getMaster())) {
            RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                    .master(redisProperties.getSentinel().getMaster());
            redisProperties.getSentinel().getNodes().forEach(s -> {
                String[] hostAndPort = s.split(":");
                sentinelConfig.sentinel(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
            });
            sentinelConfig.setDatabase(dbIndex);
            if (StringUtils.isNotBlank(redisProperties.getPassword())) {
                sentinelConfig.setPassword(redisProperties.getPassword());
            }
            return new LettuceConnectionFactory(sentinelConfig);
        }

        if (redisProperties.getCluster() != null && !redisProperties.getCluster().getNodes().isEmpty()) {
            RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
            if (StringUtils.isNotBlank(redisProperties.getPassword())) {
                clusterConfig.setPassword(redisProperties.getPassword());
            }
            return new LettuceConnectionFactory(clusterConfig);
        }

        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(
                redisProperties.getHost(), redisProperties.getPort()
        );
        standaloneConfig.setDatabase(dbIndex);
        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            standaloneConfig.setPassword(redisProperties.getPassword());
        }
        return new LettuceConnectionFactory(standaloneConfig);
    }

    public RedisTemplate<String, Object> createRedisTemplate(RedisConnectionFactory connectionFactory) {
        // Configure ObjectMapper for Redis serialization
        ObjectMapper objectMapper = JsonMapper.builder()
                // Register the JavaTimeModule to handle Java 8 Date & Time APIs (e.g., Instant)
//                .registerModule(new JavaTimeModule())
                // For better readability in Redis, disable writing dates as timestamps
                .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
//                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
//                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .build();

        // This is a crucial best practice for storing polymorphic types.
        // It stores the class name in the JSON (@class property), so Jackson knows
        // what specific class to deserialize the JSON back to. This prevents ClassCastExceptions.
//        objectMapper.activateDefaultTyping(
//                objectMapper.getPolymorphicTypeValidator(),
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );

        // Create the serializer with the configured ObjectMapper
        return getStringObjectRedisTemplate(connectionFactory, objectMapper);
    }

    private static RedisTemplate<String, Object> getStringObjectRedisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        GenericJacksonJsonRedisSerializer serializer = new GenericJacksonJsonRedisSerializer(objectMapper);

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Primary
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate() {
        LettuceConnectionFactory connectionFactory = createConnectionFactory(redisProperties.getDatabase());
        return createRedisTemplate(connectionFactory);
    }
}