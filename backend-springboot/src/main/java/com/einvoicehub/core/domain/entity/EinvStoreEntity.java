package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Entity
@Table(name = "einv_stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EinvStoreEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id; // UUID của Store

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private EinvMerchantEntity merchant;

    @Column(name = "store_name", length = 255, nullable = false)
    private String storeName;

    @Column(name = "tax_code", length = 20)
    private String taxCode;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updateAt;

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