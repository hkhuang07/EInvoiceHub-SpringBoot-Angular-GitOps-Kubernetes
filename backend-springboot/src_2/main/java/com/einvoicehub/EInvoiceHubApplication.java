package com.einvoicehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * EInvoiceHub - Hệ thống trung gian kết nối hóa đơn điện tử
 * 
 * Main application class cho Spring Boot 3.4.1 với Java 21
 * Sử dụng Virtual Threads để xử lý concurrent API calls
 */
@SpringBootApplication
@EnableAsync
public class EInvoiceHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(EInvoiceHubApplication.class, args);
    }
}