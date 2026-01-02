package com.einvoicehub.core.entity.mysql;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_credentials")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(name = "client_id", nullable = false, unique = true, length = 50)
    private String clientId;

    @Column(name = "api_key_hash", nullable = false, length = 255)
    private String apiKeyHash;

    @Column(name = "api_key_prefix", nullable = false, length = 8)
    private String apiKeyPrefix;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String scopes;

    @Column(name = "ip_whitelist", columnDefinition = "TEXT")
    private String ipWhitelist;

    @Column(name = "rate_limit_per_hour", nullable = false)
    @Builder.Default
    private Integer rateLimitPerHour = 1000;

    @Column(name = "rate_limit_per_day", nullable = false)
    @Builder.Default
    private Integer rateLimitPerDay = 10000;

    @Column(name = "request_count_hour", nullable = false)
    @Builder.Default
    private Integer requestCountHour = 0;

    @Column(name = "request_count_day", nullable = false)
    @Builder.Default
    private Integer requestCountDay = 0;

    @Column(name = "request_count_resetted_at")
    private LocalDateTime requestCountResettedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_by")
    private Long createdBy;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public boolean isExpired() {
        return expiredAt != null && expiredAt.isBefore(LocalDateTime.now());
    }

    public boolean isValid() {
        return isActive && !isExpired();
    }

    public boolean hasScope(String scope) {
        if (scopes == null || scopes.isEmpty()) {
            return false;
        }
        return scopes.contains(scope) || scopes.contains("*");
    }

    public boolean hasAnyScope(String... requiredScopes) {
        for (String scope : requiredScopes) {
            if (hasScope(scope)) {
                return true;
            }
        }
        return false;
    }

    public boolean canMakeRequest() {
        return isValid() &&
                requestCountHour < rateLimitPerHour &&
                requestCountDay < rateLimitPerDay;
    }

    public void incrementRequestCount() {
        this.requestCountHour++;
        this.requestCountDay++;
        this.lastUsedAt = LocalDateTime.now();
    }

    public void resetHourlyCount() {
        this.requestCountHour = 0;
    }

    public void resetDailyCount() {
        this.requestCountDay = 0;
        this.requestCountHour = 0;
    }

    public void updateLastUsed() {
        this.lastUsedAt = LocalDateTime.now();
    }
}