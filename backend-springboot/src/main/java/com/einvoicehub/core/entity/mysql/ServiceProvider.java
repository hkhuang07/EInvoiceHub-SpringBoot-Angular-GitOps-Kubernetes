package com.einvoicehub.core.entity.mysql;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_providers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"merchantConfigs", "invoices"})
@EqualsAndHashCode(of = "id")
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String providerCode;

    @Column(nullable = false, length = 100)
    private String providerName;

    @Column(nullable = false, length = 500)
    private String officialApiUrl;

    private String documentationUrl;
    private String supportEmail;
    private String supportPhone;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Boolean isDefault = false;

    @Builder.Default
    private Integer displayOrder = 0;

    @Column(columnDefinition = "JSON")
    private String extraConfigSchema;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "provider")
    @Builder.Default
    private List<MerchantProviderConfig> merchantConfigs = new ArrayList<>();

    @OneToMany(mappedBy = "provider")
    @Builder.Default
    private List<InvoiceMetadata> invoices = new ArrayList<>();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getDisplayName() {
        return providerName;
    }
}