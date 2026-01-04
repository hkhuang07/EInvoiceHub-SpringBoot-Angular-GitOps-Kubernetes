package com.einvoicehub.core.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Hợp nhất WebClient Configuration cho EInvoiceHub
 * Xử lý gọi API đến các nhà cung cấp (VNPT, Viettel, BKAV, MISA)
 */
@Configuration
public class WebClientConfig {

    // Cấu hình Timeout mặc định từ bản security
    @Value("${app.provider.timeout-ms:30000}")
    private int defaultConnectTimeout;

    @Value("${app.provider.read-timeout-ms:60000}")
    private int defaultReadTimeout;

    // Cấu hình riêng biệt cho từng Provider từ bản config
    @Value("${provider.vnpt.timeout-ms:30000}")
    private int vnptTimeout;

    @Value("${provider.viettel.timeout-ms:30000}")
    private int viettelTimeout;

    @Value("${provider.bkav.timeout-ms:30000}")
    private int bkavTimeout;

    @Value("${provider.misa.timeout-ms:30000}")
    private int misaTimeout;

    /**
     * Bean chính: WebClient.Builder với bộ đệm 16MB để xử lý PDF/XML lớn
     */
    @Bean
    @Primary
    public WebClient.Builder webClientBuilder() {
        // Tăng max in-memory size lên 16MB để tránh lỗi DataBufferLimitException khi nhận PDF
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .exchangeStrategies(strategies)
                .clientConnector(new ReactorClientHttpConnector(createHttpClient(defaultConnectTimeout, defaultReadTimeout)));
    }

    /** WebClient cho VNPT */
    @Bean(name = "vnptWebClient")
    public WebClient vnptWebClient(WebClient.Builder builder, @Value("${provider.vnpt.base-url:}") String baseUrl) {
        return builder.baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(createHttpClient(vnptTimeout, vnptTimeout * 2)))
                .build();
    }

    /** WebClient cho Viettel */
    @Bean(name = "viettelWebClient")
    public WebClient viettelWebClient(WebClient.Builder builder, @Value("${provider.viettel.base-url:}") String baseUrl) {
        return builder.baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(createHttpClient(viettelTimeout, viettelTimeout * 2)))
                .build();
    }

    /** WebClient cho BKAV */
    @Bean(name = "bkavWebClient")
    public WebClient bkavWebClient(WebClient.Builder builder, @Value("${provider.bkav.base-url:}") String baseUrl) {
        return builder.baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(createHttpClient(bkavTimeout, bkavTimeout * 2)))
                .build();
    }

    /** WebClient cho MISA */
    @Bean(name = "misaWebClient")
    public WebClient misaWebClient(WebClient.Builder builder, @Value("${provider.misa.base-url:}") String baseUrl) {
        return builder.baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(createHttpClient(misaTimeout, misaTimeout * 2)))
                .build();
    }

    /**
     * Helper tạo HttpClient với cấu hình Timeout Netty chuẩn xác
     */
    private HttpClient createHttpClient(int connectTimeoutMs, int readTimeoutMs) {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
                .responseTimeout(Duration.ofMillis(readTimeoutMs))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeoutMs, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(readTimeoutMs, TimeUnit.MILLISECONDS)));
    }
}