package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** mapping: Loại hóa đơn (HUB) và (NCC) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_mapping_invoice_type",
        uniqueConstraints = { @UniqueConstraint(name = "uq_map_inv_type", columnNames = {"tenant_id", "provider_id", "system_code"}) },
        indexes = { @Index(name = "idx_map_inv_type_lookup", columnList = "tenant_id, provider_id, system_code") })
public class EinvMappingInvoiceTypeEntity extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_map_inv_type_provider"))
    private EinvProviderEntity provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_type_id", nullable = false)
    private EinvInvoiceTypeEntity invoiceType;

    @Column(name = "provider_code", length = 100, nullable = false)
    private String providerCode;

    @Column(name = "description", length = 255)
    private String description;
}