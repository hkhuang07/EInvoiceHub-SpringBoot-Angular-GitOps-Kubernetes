package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** mapping: Loại hàng hóa hoaặc khác trên dòng HĐ (HUB) và (NCC) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_mapping_item_type",
        uniqueConstraints = { @UniqueConstraint(name = "uq_map_item", columnNames = {"tenant_id", "provider_id", "system_code"}) },
        indexes = { @Index(name = "idx_map_item_lookup", columnList = "tenant_id, provider_id, system_code") })
public class EinvMappingItemTypeEntity extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_map_item_provider"))
    private EinvProviderEntity provider;

    @Column(name = "system_code", length = 50, nullable = false)
    private String systemCode;

    @Column(name = "provider_code", length = 100, nullable = false)
    private String providerCode;

    @Column(name = "description", length = 255)
    private String description;
}