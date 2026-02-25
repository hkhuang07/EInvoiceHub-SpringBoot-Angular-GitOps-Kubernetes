package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** mapping: Đơn vị tính HUB và NCC */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_mapping_unit")
public class EinvMappingUnitEntity extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_map_unit_provider"))
    private EinvProviderEntity provider;

    @Column(name = "system_code", length = 50, nullable = false)
    private String systemCode;

    @Column(name = "provider_code", length = 100, nullable = false)
    private String providerCode;
}