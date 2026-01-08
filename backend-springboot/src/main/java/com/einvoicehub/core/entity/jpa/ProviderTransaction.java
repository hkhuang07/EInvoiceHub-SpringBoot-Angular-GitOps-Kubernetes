package com.einvoicehub.core.entity.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "provider_transactions")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "invoice_metadata_id")
    private Long invoiceMetadataId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "provider_code")
    private String providerCode;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "request_data", columnDefinition = "LONGTEXT")
    private String requestData;

    @Column(name = "response_data", columnDefinition = "LONGTEXT")
    private String responseData;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "processing_time_ms")
    private Long processingTimeMs;

    @CreatedDate
    @Column(name = "timestamp", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "retry_count")
    @Builder.Default
    private Integer retryCount = 0;

    @Column(name = "next_retry_at")
    private LocalDateTime nextRetryAt;

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;

    public enum TransactionType {
        CREATE_INVOICE,      // Tạo hóa đơn mới
        GET_INVOICE_STATUS,  // Lấy trạng thái hóa đơn
        CANCEL_INVOICE,      // Hủy hóa đơn
        REPLACE_INVOICE,     // Thay thế hóa đơn
        SYNC_INVOICE,        // Đồng bộ hóa đơn
        VALIDATE_INVOICE,    // Kiểm tra tính hợp lệ
        DOWNLOAD_INVOICE     // Tải hóa đơn
    }

    public enum TransactionStatus {
        PENDING,     // Đang chờ xử lý
        PROCESSING,  // Đang xử lý
        SUCCESS,     // Thành công
        FAILED,      // Thất bại
        RETRYING,    // Đang thử lại
        CANCELLED    // Đã hủy
    }
}
