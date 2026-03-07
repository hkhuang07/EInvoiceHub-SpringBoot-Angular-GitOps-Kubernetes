package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "einv_sync_queue",
       indexes = {
           @Index(name = "idx_queue_status_retry", columnList = "status, next_retry_at"),
           @Index(name = "idx_queue_invoice", columnList = "invoice_id")
       })
public class EinvSyncQueueEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    @Column(name = "tenant_id", length = 36)
    private String tenantId;

    @Column(name = "provider_id", length = 36)
    private String providerId;

    @Column(name = "invoice_id", length = 36)
    private String invoiceId;

    //ID thông điệp truyền nhận với Cơ quan Thuế
    @Column(name = "cqt_message_id", length = 100)
    private String cqtMessageId;

    //Loại sync: SUBMIT | SIGN | GET_STATUS | GET_INVOICE
    @Column(name = "sync_type", length = 50, nullable = false)
    private String syncType;

    //Trạng thái: PENDING | PROCESSING | SUCCESS | FAILED
    @Column(name = "status", length = 20, nullable = false)
    private String status = "PENDING";

    //Số lần đã retry
    @Column(name = "attempt_count", nullable = false)
    private Byte attemptCount = 0;

    //Giới hạn retry
    @Column(name = "max_attempts", nullable = false)
    private Byte maxAttempts = 5;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;
    @Column(name = "error_code", length = 50)
    private String errorCode;

    //Thời điểm retry tiếp theo
    @Column(name = "next_retry_at")
    private LocalDateTime nextRetryAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCqtMessageId() {
        return cqtMessageId;
    }

    public void setCqtMessageId(String cqtMessageId) {
        this.cqtMessageId = cqtMessageId;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Byte getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(Byte attemptCount) {
        this.attemptCount = attemptCount;
    }

    public Byte getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Byte maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public LocalDateTime getNextRetryAt() {
        return nextRetryAt;
    }

    public void setNextRetryAt(LocalDateTime nextRetryAt) {
        this.nextRetryAt = nextRetryAt;
    }
}
