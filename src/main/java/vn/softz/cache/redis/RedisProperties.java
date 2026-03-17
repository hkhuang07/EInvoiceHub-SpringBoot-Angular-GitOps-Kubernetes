package vn.softz.cache.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import vn.softz.common.utils.CryptoUtils;
import vn.softz.common.utils.StringUtils;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

    @Value("${spring.security.master-key:}")
    private String materKey;

    private String host = "localhost";
    private int port = 6379;
    private String password;
    public void setPassword(String value) {
        this.password = decrypt(value);
    }
    private Sentinel sentinel;
    private Cluster cluster;
    private int database = 0;
    private String prefix = "";

    @Data
    public static class Sentinel {
        private String master;
        private List<String> nodes;
    }

    @Data
    public static class Cluster {
        private List<String> nodes;
    }

    private String decrypt(String value) {
        try {
            return CryptoUtils.AES.decrypt(value, StringUtils.reverse(materKey));
        } catch (Exception e) {
            return value;
        }
    }
}