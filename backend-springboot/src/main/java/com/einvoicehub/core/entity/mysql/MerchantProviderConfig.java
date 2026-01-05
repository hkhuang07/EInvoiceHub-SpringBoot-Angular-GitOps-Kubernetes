package com.einvoicehub.core.entity.mysql;

import com.einvoicehub.core.converter.EncryptionConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "merchant_provider_configs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"merchant", "provider"})
@EqualsAndHashCode(of = "id")
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

    @Column(nullable = false, length = 100)
    private String usernameService;

    @Column(nullable = false, length = 500)
    @Convert(converter = EncryptionConverter.class)
    private String passwordServiceEncrypted;

    @Column(length = 100)
    private String certificateSerial;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = EncryptionConverter.class)
    private String certificateData;

    private LocalDateTime certificateExpiredAt;

    @Column(columnDefinition = "JSON")
    private String extraConfig;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Boolean isDefault = false;

    private LocalDateTime lastSyncAt;

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

    public boolean isCertificateExpired() {
        return certificateExpiredAt != null && certificateExpiredAt.isBefore(LocalDateTime.now());
    }

    public boolean hasValidCertificate() {
        return certificateData != null && !certificateData.isEmpty() && !isCertificateExpired();
    }

    public String getProviderCode() {
        return provider != null ? provider.getProviderCode() : null;
    }
}