package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "merchant")
public class EinvApiCredentialsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private MerchantEntity merchant;

    @Column(name = "client_id", length = 50, nullable = false, unique = true)
    private String clientId;

    @Column(name = "api_key_hash", length = 255, nullable = false)
    private String apiKeyHash;

    @Column(name = "api_key_prefix", length = 8, nullable = false)
    private String apiKeyPrefix;

    @Column(name = "scopes", columnDefinition = "TEXT", nullable = false)
    private String scopes; // Lưu danh sách quyền dạng JSON array

    // --- Enterprise Shell Fields (Hạ tầng bảo mật & Giới hạn tải) ---
    @Column(name = "ip_whitelist", columnDefinition = "TEXT")
    private String ipWhitelist;

    @Builder.Default
    @Column(name = "rate_limit_per_hour", nullable = false)
    private Integer rateLimitPerHour = 1000;

    @Builder.Default
    @Column(name = "rate_limit_per_day", nullable = false)
    private Integer rateLimitPerDay = 10000;

    @Builder.Default
    @Column(name = "request_count_hour", nullable = false)
    private Integer requestCountHour = 0;

    @Builder.Default
    @Column(name = "request_count_day", nullable = false)
    private Integer requestCountDay = 0;

    @Column(name = "request_count_resetted_at")
    private LocalDateTime requestCountResettedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    // --------------------------------

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}