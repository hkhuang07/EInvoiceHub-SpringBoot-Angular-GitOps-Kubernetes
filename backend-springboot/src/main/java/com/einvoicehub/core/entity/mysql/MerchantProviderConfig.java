package com.einvoicehub.core.entity.mysql;

import com.einvoicehub.core.converter.EncryptionConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "merchant_provider_configs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantProviderConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    @Column(name = "username_service", nullable = false, length = 100)
    private String usernameService;

    @Column(name = "password_service_encrypted", nullable = false, length = 500)
    @Convert(converter = EncryptionConverter.class)
    private String passwordServiceEncrypted;

    @Column(name = "certificate_serial", length = 100)
    private String certificateSerial;

    @Column(name = "certificate_data", columnDefinition = "TEXT")
    @Convert(converter = EncryptionConverter.class)
    private String certificateData;

    @Column(name = "certificate_expired_at")
    private LocalDateTime certificateExpiredAt;

    @Column(name = "extra_config", columnDefinition = "JSON")
    private String extraConfig;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    @Column(name = "last_sync_at")
    private LocalDateTime lastSyncAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public boolean isActive() {
        return isActive;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isCertificateExpired() {
        return certificateExpiredAt != null && certificateExpiredAt.isBefore(LocalDateTime.now());
    }

    public boolean hasValidCertificate() {
        return certificateData != null && !certificateData.isEmpty() && !isCertificateExpired();
    }

    public void markSynced() {
        this.lastSyncAt = LocalDateTime.now();
    }

    public String getProviderCode() {
        return provider != null ? provider.getProviderCode() : null;
    }

    public String getProviderName() {
        return provider != null ? provider.getProviderName() : null;
    }
}