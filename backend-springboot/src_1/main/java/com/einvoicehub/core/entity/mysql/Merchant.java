package com.einvoicehub.core.entity.mysql;

import com.einvoicehub.core.entity.enums.EntityStatus;
import com.einvoicehub.core.entity.enums.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "merchants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tax_code", unique = true, nullable = false, length = 20)
    private String taxCode;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "short_name", length = 100)
    private String shortName;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 100)
    private String district;

    @Column(length = 100)
    private String city;

    @Column(length = 255)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "representative_name", length = 100)
    private String representativeName;

    @Column(name = "representative_title", length = 100)
    private String representativeTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_plan", nullable = false)
    @Builder.Default
    private SubscriptionPlan subscriptionPlan = SubscriptionPlan.TRIAL;

    @Column(name = "invoice_quota", nullable = false)
    @Builder.Default
    private Integer invoiceQuota = 100;

    @Column(name = "invoice_used", nullable = false)
    @Builder.Default
    private Integer invoiceUsed = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EntityStatus status = EntityStatus.ACTIVE;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relationships
    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MerchantUser> users = new ArrayList<>();

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ApiCredential> apiCredentials = new ArrayList<>();

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MerchantProviderConfig> providerConfigs = new ArrayList<>();

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    @Builder.Default
    private List<InvoiceMetadata> invoices = new ArrayList<>();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public boolean canCreateMoreInvoices() {
        return subscriptionPlan.isUnlimited() || invoiceUsed < invoiceQuota;
    }

    public void incrementInvoiceUsage() {
        if (!canCreateMoreInvoices()) {
            throw new IllegalStateException("Invoice quota exceeded");
        }
        this.invoiceUsed++;
    }

    public void resetMonthlyUsage() {
        this.invoiceUsed = 0;
    }

    public boolean hasAvailableQuota() {
        return canCreateMoreInvoices();
    }

    public int getRemainingQuota() {
        return subscriptionPlan.isUnlimited() ? Integer.MAX_VALUE : invoiceQuota - invoiceUsed;
    }

    public void addUser(MerchantUser user) {
        users.add(user);
        user.setMerchant(this);
    }

    public void removeUser(MerchantUser user) {
        users.remove(user);
        user.setMerchant(null);
    }

    public void addApiCredential(ApiCredential credential) {
        apiCredentials.add(credential);
        credential.setMerchant(this);
    }

    public void addProviderConfig(MerchantProviderConfig config) {
        providerConfigs.add(config);
        config.setMerchant(this);
    }
}