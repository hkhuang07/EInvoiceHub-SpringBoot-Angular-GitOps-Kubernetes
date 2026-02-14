package com.einvoicehub.core.domain.entity;

import com.einvoicehub.core.domain.entity.enums.SyncType;
import com.einvoicehub.core.domain.entity.enums.SyncStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_sync_queue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "invoice")
public class EinvInvoiceSyncQueueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private EinvInvoiceMetadataEntity invoice;

    @Enumerated(EnumType.STRING)
    @Column(name = "sync_type", nullable = false)
    private SyncType syncType;

    @Builder.Default
    @Column(name = "priority", nullable = false)
    private Integer priority = 5; // Độ ưu tiên: 1-Cao nhất, 10-Thấp nhất

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", nullable = false)
    private SyncStatus status = SyncStatus.PENDING;

    @Builder.Default
    @Column(name = "attempt_count", nullable = false)
    private Integer attemptCount = 0;

    @Builder.Default
    @Column(name = "max_attempts", nullable = false)
    private Integer maxAttempts = 3;

    @Column(name = "last_attempt_at")
    private LocalDateTime lastAttemptAt;

    @Column(name = "next_retry_at")
    private LocalDateTime nextRetryAt;

    // --- Core Management Fields (Dữ liệu phục vụ xử lý lại) ---
    @Column(name = "request_data", columnDefinition = "JSON")
    private String requestData; // Dữ liệu gửi đi dạng JSON

    @Column(name = "response_data", columnDefinition = "JSON")
    private String responseData; // Dữ liệu nhận về từ API

    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    // --- Enterprise Shell Fields (Lưu vết worker xử lý) ---
    @Column(name = "processed_by", length = 100)
    private String processedBy; // Tên máy hoặc ID worker xử lý

    @Column(name = "processing_started_at")
    private LocalDateTime processingStartedAt;

    @Column(name = "processing_completed_at")
    private LocalDateTime processingCompletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    // --------------------------------

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