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
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"users", "apiCredentials", "providerConfigs", "invoices"})
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String taxCode;

    @Column(nullable = false)
    private String companyName;

    private String address;
    private String email;
    private String phone;

    @Builder.Default
    private Integer invoiceQuota = 100;

    @Builder.Default
    private Integer currentInvoiceCount = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SubscriptionPlan subscriptionPlan = SubscriptionPlan.TRIAL;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EntityStatus status = EntityStatus.ACTIVE;

    @Builder.Default
    private Boolean isDeleted = false;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    @Builder.Default
    private List<InvoiceMetadata> invoices = new ArrayList<>();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /* Business Methods */

    public String getName() {
        return companyName;
    }

    public boolean hasAvailableQuota() {
        return subscriptionPlan.isUnlimited() || currentInvoiceCount < invoiceQuota;
    }

    public void incrementInvoiceUsage() {
        if (!hasAvailableQuota()) {
            throw new IllegalStateException("Merchant invoice quota exceeded");
        }
        this.currentInvoiceCount++;
    }

    public int getRemainingQuota() {
        return subscriptionPlan.isUnlimited() ? Integer.MAX_VALUE : invoiceQuota - currentInvoiceCount;
    }
}