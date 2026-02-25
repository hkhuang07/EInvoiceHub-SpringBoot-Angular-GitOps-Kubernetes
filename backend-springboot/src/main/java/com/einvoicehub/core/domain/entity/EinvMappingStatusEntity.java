package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** mapping: Trạng thái hóa đơn của Nhà cung cấp và HUB */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_mapping_status")
public class EinvMappingStatusEntity extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_map_status_provider"))
    private EinvProviderEntity provider;

    @Column(name = "provider_status_code", length = 50, nullable = false)
    private String providerStatusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_status_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_map_status_hub"))
    private EinvInvoiceStatusEntity hubStatus;
}