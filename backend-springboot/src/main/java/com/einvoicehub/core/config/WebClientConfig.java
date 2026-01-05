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

@Configuration
public class WebClientConfig {

    @Value("${app.provider.timeout-ms:30000}")
    private int defaultTimeout;

    @Value("${provider.vnpt.timeout-ms:30000}")
    private int vnptTimeout;

    @Value("${provider.viettel.timeout-ms:30000}")
    private int viettelTimeout;

    @Value("${provider.bkav.timeout-ms:30000}")
    private int bkavTimeout;

    @Value("${provider.misa.timeout-ms:30000}")
    private int misaTimeout;

    @Bean
    @Primary
    public WebClient.Builder webClientBuilder() {
        /* Increase memory buffer to 16MB for large XML/PDF invoice processing */
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .exchangeStrategies(strategies)
                .clientConnector(new ReactorClientHttpConnector(createHttpClient(defaultTimeout, defaultTimeout * 2)));
    }

    @Bean(name = "vnptWebClient")
    public WebClient vnptWebClient(WebClient.Builder builder, @Value("${provider.vnpt.base-url:}") String url) {
        return createWebClient(builder, url, vnptTimeout);
    }

    @Bean(name = "viettelWebClient")
    public WebClient viettelWebClient(WebClient.Builder builder, @Value("${provider.viettel.base-url:}") String url) {
        return createWebClient(builder, url, viettelTimeout);
    }

    @Bean(name = "bkavWebClient")
    public WebClient bkavWebClient(WebClient.Builder builder, @Value("${provider.bkav.base-url:}") String url) {
        return createWebClient(builder, url, bkavTimeout);
    }

    @Bean(name = "misaWebClient")
    public WebClient misaWebClient(WebClient.Builder builder, @Value("${provider.misa.base-url:}") String url) {
        return createWebClient(builder, url, misaTimeout);
    }

    private WebClient createWebClient(WebClient.Builder builder, String baseUrl, int timeout) {
        return builder.clone()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(createHttpClient(timeout, timeout * 2)))
                .build();
    }

    private HttpClient createHttpClient(int connectTimeout, int readWriteTimeout) {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .responseTimeout(Duration.ofMillis(readWriteTimeout))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readWriteTimeout, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(readWriteTimeout, TimeUnit.MILLISECONDS)));
    }
}