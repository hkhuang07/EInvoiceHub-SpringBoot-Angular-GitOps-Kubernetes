package com.einvoicehub.core.entity.mysql;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_providers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_code", nullable = false, unique = true, length = 20)
    private String providerCode;

    @Column(name = "provider_name", nullable = false, length = 100)
    private String providerName;

    @Column(name = "official_api_url", nullable = false, length = 500)
    private String officialApiUrl;

    @Column(name = "documentation_url", length = 500)
    private String documentationUrl;

    @Column(name = "support_email", length = 255)
    private String supportEmail;

    @Column(name = "support_phone", length = 20)
    private String supportPhone;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "extra_config_schema", columnDefinition = "JSON")
    private String extraConfigSchema;

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

    // Relationships
    @OneToMany(mappedBy = "provider")
    @Builder.Default
    private List<MerchantProviderConfig> merchantConfigs = new ArrayList<>();

    @OneToMany(mappedBy = "provider")
    @Builder.Default
    private List<InvoiceMetadata> invoices = new ArrayList<>();

    // Business methods
    public boolean isAvailable() {
        return isActive;
    }

    public String getDisplayName() {
        return providerName;
    }
}