package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"merchant", "invoiceType", "registration"})
public class EinvInvoiceTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private EinvMerchantEntity merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_type_id")
    private EinvInvoiceTypeEntity invoiceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_registration_id")
    private EinvInvoiceRegistrationEntity registration;

    @Column(name = "template_code", length = 20, nullable = false)
    private String templateCode;
    @Column(name = "symbol_code", length = 10, nullable = false)

    private String symbolCode;

    @Builder.Default
    @Column(name = "current_number", nullable = false)
    private Integer currentNumber = 0;

    @Builder.Default
    @Column(name = "min_number", nullable = false)
    private Integer minNumber = 1;

    @Builder.Default
    @Column(name = "max_number", nullable = false)
    private Integer maxNumber = 99999999;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "status_id")
    private Integer statusId; // ID trạng thái mở rộng

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // --- Enterprise Shell Fields ---
    @Column(name = "provider_id", length = 36)
    private String providerId;

    @Column(name = "provider_serial_id", length = 50)
    private String providerSerialId;

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