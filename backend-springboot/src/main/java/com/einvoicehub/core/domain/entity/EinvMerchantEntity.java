package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "merchants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EinvMerchantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", length = 50, nullable = false, unique = true)
    private String tenantId; // Định danh từ hệ thống PosORA

    @Column(name = "company_name", length = 255, nullable = false)
    private String companyName;

    @Column(name = "tax_code", length = 20, nullable = false)
    private String taxCode;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EinvStoreEntity> stores;

    @PrePersist
    protected void onCreate() {
        if (this.isActive == null) this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onUpdate() {
        if (this.isActive == null) this.isActive = true;
        this.updateAt = LocalDateTime.now();
    }
}