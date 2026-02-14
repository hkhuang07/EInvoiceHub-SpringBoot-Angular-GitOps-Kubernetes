package com.einvoicehub.core.domain.entity;
import com.einvoicehub.core.domain.entity.enums.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "merchants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EinvMerchantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tax_code", length = 20, nullable = false, unique = true)
    private String taxCode;

    @Column(name = "company_name", length = 255, nullable = false)
    private String companyName;

    @Column(name = "short_name", length = 100)
    private String shortName;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "representative_name", length = 100)
    private String representativeName;

    @Column(name = "representative_title", length = 100)
    private String representativeTitle;

    @Column(name = "tax_authority_code", length = 10)
    private String taxAuthorityCode;

    // --- Enterprise Shell Fields ---
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "subscription_plan", nullable = false)
    private SubscriptionPlan subscriptionPlan = SubscriptionPlan.TRIAL;

    @Builder.Default
    @Column(name = "invoice_quota", nullable = false)
    private Integer invoiceQuota = 100;

    @Builder.Default
    @Column(name = "invoice_used", nullable = false)
    private Integer invoiceUsed = 0;

    @Builder.Default
    @Column(name = "is_using_hsm", nullable = false)
    private Boolean isUsingHsm = false;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}