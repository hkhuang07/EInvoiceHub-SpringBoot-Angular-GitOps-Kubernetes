package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tax_authority_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "invoice")
public class EinvTaxAuthorityResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private EinvInvoiceEntity invoice;

    @Column(name = "cqt_code", length = 50, nullable = false)
    private String cqtCode; // Mã Cơ quan Thuế cấp

    @Column(name = "status_from_cqt", length = 50, nullable = false)
    private String statusFromCqt; // APPROVED, REJECTED, ...

    @Column(name = "processing_code", length = 100)
    private String processingCode;

    @Column(name = "signature_data", columnDefinition = "TEXT")
    private String signatureData; // Chữ ký số từ Cơ quan Thuế

    // --- Enterprise Shell Fields ---
    @Column(name = "raw_response", columnDefinition = "JSON")
    private String rawResponse; // Nội dung phản hồi thô dạng JSON

    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt; // Thời điểm nhận thực tế

    @Column(name = "processed_at")
    private LocalDateTime processedAt; // Thời điểm hệ thống xử lý xong

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    // --------------------------------

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.receivedAt == null) {
            this.receivedAt = LocalDateTime.now();
        }
    }
}