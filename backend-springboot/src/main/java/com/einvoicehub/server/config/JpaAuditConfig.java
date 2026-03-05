package com.einvoicehub.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditConfig {
    // Cấu hình @EnableJpaAuditing để kích hoạt tính năng audit tự động
    // JPA Auditing sẽ tự động set createdDate và updatedDate khi save entity
}

