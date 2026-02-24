package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Entity
@Table(name = "einv_store_provider")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = {"merchant", "store", "provider"})
public class EinvStoreProviderEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private EinvMerchantEntity merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private EinvStoreEntity store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private EinvProviderEntity provider;

    @Column(name = "partner_id", length = 255)
    private String partnerId;

    @Column(name = "partner_token", length = 500)
    private String partnerToken;

    @Column(name = "partner_usr", length = 255)
    private String partnerUsr;

    @Column(name = "partner_pwd", length = 500)
    private String partnerPwd;

    @Column(name = "integration_url", length = 500)
    private String integrationUrl;

    @Column(name = "tax_code", length = 20)
    private String taxCode;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "integrated_date")
    private LocalDateTime integratedDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) this.status = 0;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}