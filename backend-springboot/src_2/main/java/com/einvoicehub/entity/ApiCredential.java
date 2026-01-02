package com.einvoicehub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Api Credential Entity - Thông tin xác thực API của Merchant
 * 
 * Lưu trữ thông tin API Key và cấu hình bảo mật cho mỗi merchant
 */
@Entity
@Table(name = "api_credentials")
public class ApiCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "client_id", nullable = false, unique = true, length = 50)
    private String clientId;

    @Column(name = "api_key", nullable = false, length = 128)
    private String apiKey;

    @Column(name = "api_secret", length = 256)
    private String apiSecret;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "rate_limit_per_minute")
    private Integer rateLimitPerMinute = 100;

    @Column(name = "ip_whitelist", length = 500)
    private String ipWhitelist;

    @Column(name = "allowed_providers", length = 255)
    private String allowedProviders; // Comma-separated list of provider codes

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", insertable = false, updatable = false)
    private Merchant merchant;

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getRateLimitPerMinute() {
        return rateLimitPerMinute;
    }

    public void setRateLimitPerMinute(Integer rateLimitPerMinute) {
        this.rateLimitPerMinute = rateLimitPerMinute;
    }

    public String getIpWhitelist() {
        return ipWhitelist;
    }

    public void setIpWhitelist(String ipWhitelist) {
        this.ipWhitelist = ipWhitelist;
    }

    public String getAllowedProviders() {
        return allowedProviders;
    }

    public void setAllowedProviders(String allowedProviders) {
        this.allowedProviders = allowedProviders;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(LocalDateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    // Business methods
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isProviderAllowed(String providerCode) {
        if (allowedProviders == null || allowedProviders.isBlank()) {
            return true; // Allow all providers if not restricted
        }
        String[] allowed = allowedProviders.split(",");
        for (String allowedProvider : allowed) {
            if (allowedProvider.trim().equalsIgnoreCase(providerCode)) {
                return true;
            }
        }
        return false;
    }
}