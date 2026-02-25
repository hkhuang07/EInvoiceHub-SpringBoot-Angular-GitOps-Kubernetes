package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

/** cấu hình tích hợp Store ↔ NCC HĐĐT (16 fields trong tl 4.2/4.3)*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_store_provider",
        indexes = {
                @Index(name = "idx_sp_tenant", columnList = "tenant_id"),
                @Index(name = "idx_sp_store", columnList = "store_id"),
                @Index(name = "idx_sp_provider", columnList = "provider_id"),
                @Index(name = "idx_sp_status", columnList = "status")
        })
public class EinvStoreProviderEntity extends TenantEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id; // UUID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sp_store"))
    private EinvStoreEntity store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sp_provider"))
    private EinvProviderEntity provider;

    @Column(name = "sign_type")
    private Integer signType; // 0:Token, 1:HSM, 2:SmartCA

    @Column(name = "is_two_step_signing")
    private Boolean isTwoStepSigning = false;

    @Column(name = "partner_id")
    private String partnerId; // BKAV: PartnerGUID | VNPT: Account

    @Column(name = "app_id", length = 100)
    private String appId;

    @Column(name = "fkey_prefix", length = 50)
    private String fkeyPrefix; // Fkey cho VNPT

    @Column(name = "partner_token", columnDefinition = "TEXT")
    private String partnerToken; // JWT Token dài

    @Column(name = "partner_pwd", columnDefinition = "TEXT")
    private String partnerPwd;

    @Column(name = "partner_usr")
    private String partnerUsr;

    @Column(name = "username_service", length = 100)
    private String usernameService;

    @Column(name = "password_service")
    private String passwordService;

    @Column(name = "integration_url", length = 500)
    private String integrationUrl;

    @Column(name = "tax_code", length = 20)
    private String taxCode; // MST cho phép override

    @Column(name = "status", nullable = false)
    private Integer status = 0; // 0:Mới, 1:Thành công, 2:Lỗi, 8:Inactive

    @Column(name = "integrated_date")
    private LocalDateTime integratedDate;
}