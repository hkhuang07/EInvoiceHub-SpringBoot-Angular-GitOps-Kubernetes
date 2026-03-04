package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/** Thực thể Quản lý Merchant/Tenant.*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "merchants",
        uniqueConstraints = { @UniqueConstraint(name = "uq_tenant_id", columnNames = "tenant_id") })
public class MerchantEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tenant_id", length = 50, nullable = false)
    private String tenantId;

    @Column(name = "company_name", length = 255, nullable = false)
    private String companyName;

    @Column(name = "tax_code", length = 20, nullable = false)
    private String taxCode;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EinvStoreEntity> stores;
}