package com.einvoicehub.core.entity.jpa;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_credentials")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "merchant")
@EqualsAndHashCode(of = "id")
public class ApiCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(name = "client_id", nullable = false, unique = true, length = 50)
    private String clientId;

    @Column(name = "api_key_hash", nullable = false)
    private String apiKeyHash;

    @Column(name = "api_key_prefix", nullable = false, length = 8)
    private String apiKeyPrefix;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String scopes;

    @Column(name = "ip_whitelist", columnDefinition = "TEXT")
    private String ipWhitelist;

    @Builder.Default
    private Integer rateLimitPerHour = 1000;

    @Builder.Default
    private Integer rateLimitPerDay = 10000;

    @Builder.Default
    private Integer requestCountHour = 0;

    @Builder.Default
    private Integer requestCountDay = 0;

    private LocalDateTime requestCountResettedAt;
    private LocalDateTime expiredAt;
    private LocalDateTime lastUsedAt;

    @Builder.Default
    private Boolean isActive = true;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /* Business Logic */

    public boolean isValid() {
        boolean isExpired = expiredAt != null && expiredAt.isBefore(LocalDateTime.now());
        return isActive && !isExpired;
    }

    public boolean hasScope(String requiredScope) {
        if (scopes == null || scopes.isEmpty()) return false;
        return scopes.contains(requiredScope) || scopes.contains("*");
    }

    public boolean canMakeRequest() {
        return isValid() && requestCountHour < rateLimitPerHour && requestCountDay < rateLimitPerDay;
    }

    public void incrementRequestCount() {
        this.requestCountHour++;
        this.requestCountDay++;
        this.lastUsedAt = LocalDateTime.now();
    }

    public void resetLimits(boolean daily) {
        this.requestCountHour = 0;
        if (daily) this.requestCountDay = 0;
        this.requestCountResettedAt = LocalDateTime.now();
    }
}