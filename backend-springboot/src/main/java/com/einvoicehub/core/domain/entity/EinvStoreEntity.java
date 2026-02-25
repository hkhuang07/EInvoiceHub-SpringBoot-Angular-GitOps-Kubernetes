package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** Thực thể Quản lý Chi nhánh (Store) thuộc Merchant. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_stores",
        indexes = { @Index(name = "idx_sp_store", columnList = "id") })
public class EinvStoreEntity extends TenantEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id; // UUID của Store

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_store_tenant"))
    private MerchantEntity merchant;

    @Column(name = "store_name", length = 255, nullable = false)
    private String storeName;

    @Column(name = "tax_code", length = 20)
    private String taxCode;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}