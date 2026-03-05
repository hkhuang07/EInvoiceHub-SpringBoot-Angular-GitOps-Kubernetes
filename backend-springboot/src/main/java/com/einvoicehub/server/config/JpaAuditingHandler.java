package com.einvoicehub.server.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class JpaAuditingHandler implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
                return Optional.of("system");
            }

            String username = authentication.getName();
            return Optional.of(username != null && !username.isEmpty() ? username : "system");
        } catch (Exception e) {
            return Optional.of("system");
        }
    }
}