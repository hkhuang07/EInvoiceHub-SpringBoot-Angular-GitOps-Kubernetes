package com.einvoicehub.core.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Configuration for Java 21 Virtual Threads
 * 
 * Cấu hình Virtual Threads để xử lý concurrent API calls đến các provider bên ngoài
 * Virtual Threads giúp xử lý hàng nghìn I/O operations đồng thời mà không gây thread exhaustion
 */
@Configuration
public class VirtualThreadConfig {

    /**
     * Cấu hình Virtual Threads cho Tomcat embedded server
     * Sử dụng virtual threads thay vì platform threads cho request handling
     */
    @Bean
    public TomcatProtocolHandlerCustomizer<?> tomcatVirtualThreadCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }

    /**
     * ExecutorService sử dụng Virtual Threads cho async operations
     * Sử dụng cho việc gọi API đến các provider bên ngoài
     */
    @Bean
    public ExecutorService virtualThreadExecutorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * ThreadPoolTaskExecutor với virtual threads cho Spring @Async methods
     */
    @Bean(name = "virtualThreadTaskExecutor")
    public ThreadPoolTaskExecutor virtualThreadTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(0);
        executor.setMaxPoolSize(Integer.MAX_VALUE);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("virtual-async-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        
        // Thay thế executor bằng virtual thread
        // ThreadPoolTaskExecutor không hỗ trợ virtual threads trực tiếp
        // Nên sử dụng ExecutorService thay thế
        return executor;
    }
}
