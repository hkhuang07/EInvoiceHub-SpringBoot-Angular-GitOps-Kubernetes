package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** mapping: Phương thức thanh toán (HUB) và (NCC) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_mapping_payment_method",
        uniqueConstraints = { @UniqueConstraint(name = "uq_map_pay", columnNames = {"tenant_id", "provider_id", "system_code"}) },
        indexes = { @Index(name = "idx_map_pay_lookup", columnList = "tenant_id, provider_id, system_code") })
public class EinvMappingPaymentMethodEntity extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_map_pay_provider"))
    private EinvProviderEntity provider;

    @Column(name = "system_code", length = 50, nullable = false)
    private String systemCode;

    @Column(name = "provider_code", length = 100, nullable = false)
    private String providerCode;

    @Column(name = "description", length = 255)
    private String description;
}