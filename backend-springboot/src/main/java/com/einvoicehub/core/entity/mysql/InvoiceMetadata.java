package com.einvoicehub.core.entity.mysql;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices_metadata")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"merchant", "provider", "providerConfig"})
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

    private String clientRequestId;
    private String providerCode;
    private String invoiceNumber;
    private String symbolCode;
    private String invoiceTypeCode;
    private String templateCode;

    private String sellerName;
    private String sellerTaxCode;
    @Column(columnDefinition = "TEXT")
    private String sellerAddress;

    private String buyerName;
    private String buyerTaxCode;
    private String buyerEmail;
    private String buyerPhone;
    @Column(columnDefinition = "TEXT")
    private String buyerAddress;

    @Builder.Default
    private BigDecimal subtotalAmount = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Builder.Default
    private String currencyCode = "VND";

    private LocalDate issueDate;
    private LocalDateTime signedAt;
    private LocalDateTime sentToProviderAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    @Column(columnDefinition = "TEXT")
    private String statusMessage;

    private String mongoPayloadId;
    private String providerTransactionCode;
    private String providerErrorCode;

    @Column(columnDefinition = "TEXT")
    private String providerErrorMessage;

    @Builder.Default
    private Boolean isDeleted = false;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /* Status Helpers */

    public void markAsSent() {
        this.status = InvoiceStatus.SENT_TO_PROVIDER;
        this.sentToProviderAt = LocalDateTime.now();
    }

    public void markAsSuccess(String txCode) {
        this.status = InvoiceStatus.SUCCESS;
        this.providerTransactionCode = txCode;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsFailed(String code, String message) {
        this.status = InvoiceStatus.FAILED;
        this.providerErrorCode = code;
        this.providerErrorMessage = message;
    }
}