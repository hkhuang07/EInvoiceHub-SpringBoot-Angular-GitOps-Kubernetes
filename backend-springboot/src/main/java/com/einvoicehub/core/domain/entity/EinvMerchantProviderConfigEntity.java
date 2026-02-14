package com.einvoicehub.core.domain.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "merchant_provider_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"merchant", "provider"})
public class EinvMerchantProviderConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private EinvMerchantEntity merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private EinvServiceProviderEntity provider;

    @Column(name = "partner_id", length = 50)
    private String partnerId;

    @Column(name = "partner_token", length = 500)
    private String partnerToken;

    @Column(name = "tax_code", length = 20)
    private String taxCode;

    @Column(name = "integration_url", length = 500)
    private String integrationUrl;

    @Column(name = "integrated_date")
    private LocalDateTime integratedDate;

    @Column(name = "username_service", length = 100, nullable = false)
    private String usernameService;

    @Column(name = "password_service_encrypted", length = 500, nullable = false)
    private String passwordServiceEncrypted;

    @Column(name = "certificate_serial", length = 100)
    private String certificateSerial;

    @Column(name = "certificate_data", columnDefinition = "TEXT")
    private String certificateData;

    @Column(name = "certificate_chain", columnDefinition = "LONGTEXT")
    private String certificateChain;

    @Column(name = "certificate_expired_at")
    private LocalDateTime certificateExpiredAt;

    @Column(name = "extra_config", columnDefinition = "JSON")
    private String extraConfig;

    // --- Enterprise Shell Fields ---
    @Builder.Default
    @Column(name = "is_test_mode", nullable = false)
    private Boolean isTestMode = false;

    @Column(name = "encrypted_password_storage", length = 500)
    private String encryptedPasswordStorage;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @Column(name = "last_sync_at")
    private LocalDateTime lastSyncAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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