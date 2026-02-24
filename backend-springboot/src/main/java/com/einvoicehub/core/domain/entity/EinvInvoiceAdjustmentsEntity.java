package com.einvoicehub.core.domain.entity;

import com.einvoicehub.core.domain.entity.enums.AdjustmentType;
import com.einvoicehub.core.domain.entity.enums.AdjustmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_adjustments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "originalInvoice")
public class EinvInvoiceAdjustmentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_invoice_id", nullable = false)
    private EinvInvoiceEntity originalInvoice;

    @Enumerated(EnumType.STRING)
    @Column(name = "adjustment_type", nullable = false)
    private AdjustmentType adjustmentType;

    // --- Agreement Information ---
    @Column(name = "agreement_number", length = 50)
    private String agreementNumber;

    @Column(name = "agreement_date")
    private LocalDate agreementDate;

    @Column(name = "agreement_content", columnDefinition = "TEXT")
    private String agreementContent;

    @Column(name = "signers", columnDefinition = "TEXT")
    private String signers; // JSON list of signers

    @Column(name = "reason_code", length = 50)
    private String reasonCode;

    @Column(name = "reason_description", length = 500)
    private String reasonDescription;

    // --- Financial Comparison (Precision 18,2) ---
    @Column(name = "old_subtotal_amount", precision = 18, scale = 2)
    private BigDecimal oldSubtotalAmount;

    @Column(name = "old_tax_amount", precision = 18, scale = 2)
    private BigDecimal oldTaxAmount;

    @Column(name = "old_total_amount", precision = 18, scale = 2)
    private BigDecimal oldTotalAmount;

    @Column(name = "new_subtotal_amount", precision = 18, scale = 2)
    private BigDecimal newSubtotalAmount;

    @Column(name = "new_tax_amount", precision = 18, scale = 2)
    private BigDecimal newTaxAmount;

    @Column(name = "new_total_amount", precision = 18, scale = 2)
    private BigDecimal newTotalAmount;

    @Column(name = "difference_amount", precision = 18, scale = 2)
    private BigDecimal differenceAmount;

    // --- Status & Workflow ---
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", nullable = false)
    private AdjustmentStatus status = AdjustmentStatus.PENDING;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    // --- Shell Fields (CQT Integration) ---
    @Builder.Default
    @Column(name = "submitted_to_cqt", nullable = false)
    private Boolean submittedToCqt = false;

    @Column(name = "cqt_response_code", length = 50)
    private String cqtResponseCode;

    @Column(name = "cqt_response_message", columnDefinition = "TEXT")
    private String cqtResponseMessage;

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