package com.einvoicehub.core.entity.mysql;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices_metadata")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private ServiceProvider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_config_id")
    private MerchantProviderConfig providerConfig;

    @Column(name = "client_request_id", length = 100)
    private String clientRequestId;

    @Column(name = "provider_code", length = 20)
    private String providerCode;

    // Thông tin hóa đơn
    @Column(name = "invoice_number", length = 20)
    private String invoiceNumber;

    @Column(name = "symbol_code", length = 10)
    private String symbolCode;

    @Column(name = "invoice_type_code", length = 10)
    private String invoiceTypeCode;

    @Column(name = "template_code", length = 20)
    private String templateCode;

    // Thông tin người bán
    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "seller_tax_code", length = 20)
    private String sellerTaxCode;

    @Column(name = "seller_address", columnDefinition = "TEXT")
    private String sellerAddress;

    // Thông tin người mua
    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "buyer_tax_code", length = 20)
    private String buyerTaxCode;

    @Column(name = "buyer_email", length = 255)
    private String buyerEmail;

    @Column(name = "buyer_phone", length = 20)
    private String buyerPhone;

    @Column(name = "buyer_address", columnDefinition = "TEXT")
    private String buyerAddress;

    // Thông tin tài chính
    @Column(name = "subtotal_amount", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal subtotalAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "currency_code", length = 3, nullable = false)
    @Builder.Default
    private String currencyCode = "VND";

    @Column(name = "exchange_rate", precision = 10, scale = 4, nullable = false)
    @Builder.Default
    private BigDecimal exchangeRate = BigDecimal.ONE;

    // Thời gian
    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "accounting_date")
    private LocalDate accountingDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    // Trạng thái
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    @Column(name = "status_message", columnDefinition = "TEXT")
    private String statusMessage;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Column(name = "replaced_by_invoice_id")
    private Long replacedByInvoiceId;

    // Liên kết MongoDB
    @Column(name = "mongo_payload_id", length = 50)
    private String mongoPayloadId;

    @Column(name = "mongo_transaction_id", length = 50)
    private String mongoTransactionId;

    // Tracking timestamps
    @Column(name = "signed_at")
    private LocalDateTime signedAt;

    @Column(name = "sent_to_provider_at")
    private LocalDateTime sentToProviderAt;

    @Column(name = "received_from_provider_at")
    private LocalDateTime receivedFromProviderAt;

    @Column(name = "delivered_to_buyer_at")
    private LocalDateTime deliveredToBuyerAt;

    // Provider response
    @Column(name = "provider_transaction_code", length = 100)
    private String providerTransactionCode;

    @Column(name = "provider_error_code", length = 50)
    private String providerErrorCode;

    @Column(name = "provider_error_message", columnDefinition = "TEXT")
    private String providerErrorMessage;

    // Soft delete
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    // Timestamps
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public boolean isDraft() {
        return status == InvoiceStatus.DRAFT;
    }

    public boolean isPending() {
        return status == InvoiceStatus.PENDING || status == InvoiceStatus.SIGNING;
    }

    public boolean isSuccessful() {
        return status == InvoiceStatus.SUCCESS;
    }

    public boolean isFailed() {
        return status == InvoiceStatus.FAILED;
    }

    public boolean isCancelled() {
        return status == InvoiceStatus.CANCELLED;
    }

    public boolean isTerminal() {
        return status.isTerminal();
    }

    public boolean isError() {
        return status.isError();
    }

    public boolean canBeCancelled() {
        return status == InvoiceStatus.DRAFT || status == InvoiceStatus.PENDING ||
                status == InvoiceStatus.SIGNING || status == InvoiceStatus.SENT_TO_PROVIDER;
    }

    public boolean canBeReplaced() {
        return status == InvoiceStatus.SUCCESS;
    }

    public void markAsSentToProvider() {
        this.status = InvoiceStatus.SENT_TO_PROVIDER;
        this.sentToProviderAt = LocalDateTime.now();
    }

    public void markAsSuccess(String providerTransactionCode) {
        this.status = InvoiceStatus.SUCCESS;
        this.providerTransactionCode = providerTransactionCode;
        this.receivedFromProviderAt = LocalDateTime.now();
    }

    public void markAsFailed(String errorCode, String errorMessage) {
        this.status = InvoiceStatus.FAILED;
        this.providerErrorCode = errorCode;
        this.providerErrorMessage = errorMessage;
        this.receivedFromProviderAt = LocalDateTime.now();
    }

    public void markAsCancelled(String reason) {
        this.status = InvoiceStatus.CANCELLED;
        this.cancellationReason = reason;
    }

    public String getFormattedInvoiceNumber() {
        if (symbolCode != null && !symbolCode.isEmpty()) {
            return symbolCode + "/" + invoiceNumber;
        }
        return invoiceNumber;
    }

    public String getDisplayAmount() {
        return String.format("%s %s", currencyCode, totalAmount.toString());
    }
}