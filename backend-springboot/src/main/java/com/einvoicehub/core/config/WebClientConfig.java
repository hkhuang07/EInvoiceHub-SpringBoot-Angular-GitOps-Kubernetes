package com.einvoicehub.core.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * WebClient Configuration cho external API calls
 * 
 * Cấu hình WebClient với connection pooling và timeout settings
 * Sử dụng cho việc gọi API đến các nhà cung cấp hóa đơn (VNPT, Viettel, BKAV, MISA)
 */
@Configuration
public class WebClientConfig {

    @Value("${providers.vnpt.timeout:30000}")
    private int vnptTimeout;
    
    @Value("${providers.viettel.timeout:30000}")
    private int viettelTimeout;
    
    @Value("${providers.bkav.timeout:30000}")
    private int bkavTimeout;
    
    @Value("${providers.misa.timeout:30000}")
    private int misaTimeout;

    /**
     * Default WebClient builder với cấu hình chung
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        // Tăng max in-memory size cho large JSON payloads
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer
                .defaultCodecs()
                .maxInMemorySize(16 * 1024 * 1024)) // 16MB
            .build();

        return WebClient.builder()
            .exchangeStrategies(strategies)
            .codecs(configurer -> configurer
                .defaultCodecs()
                .maxInMemorySize(16 * 1024 * 1024));
    }

    /**
     * WebClient cho VNPT API
     */
    @Bean(name = "vnptWebClient")
    public WebClient vnptWebClient(WebClient.Builder builder,
                                   @Value("${providers.vnpt.base-url}") String baseUrl) {
        HttpClient httpClient = createHttpClient(vnptTimeout);
        
        return builder
            .baseUrl(baseUrl)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    /**
     * WebClient cho Viettel API
     */
    @Bean(name = "viettelWebClient")
    public WebClient viettelWebClient(WebClient.Builder builder,
                                      @Value("${providers.viettel.base-url}") String baseUrl) {
        HttpClient httpClient = createHttpClient(viettelTimeout);
        
        return builder
            .baseUrl(baseUrl)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    /**
     * WebClient cho BKAV API
     */
    @Bean(name = "bkavWebClient")
    public WebClient bkavWebClient(WebClient.Builder builder,
                                   @Value("${providers.bkav.base-url}") String baseUrl) {
        HttpClient httpClient = createHttpClient(bkavTimeout);
        
        return builder
            .baseUrl(baseUrl)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    /**
     * WebClient cho MISA API
     */
    @Bean(name = "misaWebClient")
    public WebClient misaWebClient(WebClient.Builder builder,
                                   @Value("${providers.misa.base-url}") String baseUrl) {
        HttpClient httpClient = createHttpClient(misaTimeout);
        
        return builder
            .baseUrl(baseUrl)
            .build();
    }

    /**
     * Tạo HttpClient với timeout configuration
     */
    private HttpClient createHttpClient(int timeoutMs) {
        return HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMs)
            .responseTimeout(Duration.ofMillis(timeoutMs))
            .doOnConnected(conn -> conn
                .addHandlerLast(new ReadTimeoutHandler(timeoutMs, TimeUnit.MILLISECONDS))
                .addHandlerLast(new WriteTimeoutHandler(timeoutMs, TimeUnit.MILLISECONDS)));
    }
}
