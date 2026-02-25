package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

/** hàng chờ xử lý bất đồng bộ */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_sync_queue",
        indexes = {
                @Index(name = "idx_queue_status_retry", columnList = "status, next_retry_at"),
                @Index(name = "idx_queue_invoice", columnList = "invoice_id")
        })
public class EinvSyncQueueEntity extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false, foreignKey = @ForeignKey(name = "fk_queue_invoice"))
    private EinvInvoiceEntity invoice;

    @Column(name = "cqt_message_id", length = 100)
    private String cqtMessageId;

    @Column(name = "sync_type", length = 50, nullable = false)
    private String syncType; // SUBMIT | SIGN | GET_STATUS

    @Column(name = "status", length = 20, nullable = false)
    private String status = "PENDING";

    @Column(name = "attempt_count", nullable = false)
    private Integer attemptCount = 0;

    @Column(name = "max_attempts", nullable = false)
    private Integer maxAttempts = 3;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Column(name = "next_retry_at")
    private LocalDateTime nextRetryAt;
}