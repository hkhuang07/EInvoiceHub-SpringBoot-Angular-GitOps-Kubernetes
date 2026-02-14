package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_registrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"merchant", "status"})
public class EinvInvoiceRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private EinvMerchantEntity merchant; // Liên kết doanh nghiệp sở hữu

    @Column(name = "registration_number", length = 50)
    private String registrationNumber; // Số quyết định của CQT

    @Column(name = "from_number", nullable = false)
    private Long fromNumber;

    @Column(name = "to_number", nullable = false)
    private Long toNumber;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Builder.Default
    @Column(name = "current_number", nullable = false)
    private Long currentNumber = 0L; // Số hiện tại đã sử dụng

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate; // Ngày có hiệu lực (SQL DATE)

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private EinvRegistrationStatusEntity status; // Quan hệ với registration_statuses

    // --- Enterprise Shell Fields ---
    @Column(name = "issued_by", length = 255)
    private String issuedBy; // Cơ quan Thuế phê duyệt

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

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