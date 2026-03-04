package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** mapping: Mã loại thuế (HUB) và (NCC) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_mapping_tax_type",
        uniqueConstraints = { @UniqueConstraint(name = "uq_map_tax", columnNames = {"tenant_id", "provider_id", "system_code"}) },
        indexes = { @Index(name = "idx_map_tax_lookup", columnList = "tenant_id, provider_id, system_code") })
public class EinvMappingTaxTypeEntity extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_map_tax_provider"))
    private EinvProviderEntity provider;

    @Column(name = "system_code", length = 50, nullable = false)
    private String systemCode; // Mã thuế : VAT0, VAT5, VAT10, EXEMPT

    @Column(name = "provider_code", length = 100, nullable = false)
    private String providerCode;

    @Column(name = "description", length = 255)
    private String description;
}