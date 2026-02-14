package com.einvoicehub.core.domain.entity;

import com.einvoicehub.core.domain.entity.*;
import com.einvoicehub.core.domain.entity.enums.IssuanceMethod;
import com.einvoicehub.core.domain.entity.enums.AdjustmentType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"merchant", "provider", "providerConfig", "invoiceTemplate", "invoiceStatus", "paymentMethodEntity", "replacedBy", "deletedByUser"})
public class EinvInvoiceMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Relationships (Hierarchical Core) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private EinvMerchantEntity merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private EinvServiceProviderEntity provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_config_id")
    private EinvMerchantProviderConfigEntity providerConfig;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_template_id")
    private EinvInvoiceTemplateEntity invoiceTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private EinvInvoiceStatusEntity invoiceStatus;

    // Liên kết với bảng payment_methods qua trường method_code
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method", referencedColumnName = "method_code")
    private EinvPaymentMethodEntity paymentMethodEntity;

    // --- Identifiers & References ---
    @Column(name = "client_request_id", length = 100)
    private String clientRequestId;

    @Column(name = "partner_invoice_id", length = 50)
    private String partnerInvoiceId;

    @Column(name = "invoice_number", length = 20)
    private String invoiceNumber;

    @Column(name = "symbol_code", length = 10)
    private String symbolCode;

    @Column(name = "template_code", length = 20)
    private String templateCode;

    @Column(name = "invoice_type_code", length = 10)
    private String invoiceTypeCode;

    @Builder.Default
    @Column(name = "is_summary_invoice", nullable = false)
    private Boolean isSummaryInvoice = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "issuance_method", nullable = false)
    private IssuanceMethod issuanceMethod = IssuanceMethod.STANDARD;

    // --- Enterprise Shell Fields (Identifiers) ---
    @Column(name = "lookup_code", length = 50, unique = true)
    private String lookupCode; // Mã tra cứu hóa đơn

    @Column(name = "cqt_code", length = 50)
    private String cqtCode; // Mã Cơ quan Thuế cấp

    @Column(name = "provider_transaction_id", length = 100)
    private String providerTransactionId;

    // --- Seller Information (Flattened) ---
    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "seller_tax_code", length = 20)
    private String sellerTaxCode;

    @Column(name = "seller_address", columnDefinition = "TEXT")
    private String sellerAddress;

    // --- Buyer Information (Flattened) ---
    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "buyer_tax_code", length = 20)
    private String buyerTaxCode;

    @Column(name = "buyer_id_no", length = 20)
    private String buyerIdNo;

    @Column(name = "buyer_full_name", length = 200)
    private String buyerFullName;

    @Column(name = "buyer_email")
    private String buyerEmail;

    @Column(name = "buyer_phone", length = 20)
    private String buyerPhone;

    @Column(name = "buyer_mobile", length = 50)
    private String buyerMobile;

    @Column(name = "buyer_address", columnDefinition = "TEXT")
    private String buyerAddress;

    @Column(name = "buyer_bank_account", length = 50)
    private String buyerBankAccount;

    @Column(name = "buyer_bank_name", length = 200)
    private String buyerBankName;

    @Column(name = "buyer_budget_code", length = 20)
    private String buyerBudgetCode;

    // --- Financial Amounts (DECIMAL 18,2) ---
    @Builder.Default
    @Column(name = "subtotal_amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal subtotalAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "tax_amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "discount_amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "total_amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "gross_amount", precision = 18, scale = 2)
    private BigDecimal grossAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "net_amount", precision = 18, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode = "VND";

    @Builder.Default
    @Column(name = "exchange_rate", precision = 10, scale = 4, nullable = false)
    private BigDecimal exchangeRate = BigDecimal.ONE;

    // --- Adjustment / History ---
    @Column(name = "org_invoice_id", length = 36)
    private String originalInvoiceId;

    @Column(name = "org_invoice_form", length = 50)
    private String originalInvoiceForm;

    @Column(name = "org_invoice_series", length = 50)
    private String originalInvoiceSeries;

    @Column(name = "org_invoice_no", length = 50)
    private String originalInvoiceNo;

    @Column(name = "org_invoice_date")
    private LocalDateTime originalInvoiceDate;

    @Column(name = "org_invoice_reason", length = 500)
    private String originalInvoiceReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "adjustment_type")
    private AdjustmentType adjustmentType;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replaced_by_invoice_id")
    private EinvInvoiceMetadataEntity replacedBy;

    // --- Dates & Status ---
    @Column(name = "status_message", columnDefinition = "TEXT")
    private String statusMessage;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "accounting_date")
    private LocalDate accountingDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    // --- Timestamps Shell Fields ---
    @Column(name = "signed_at")
    private LocalDateTime signedAt;

    @Column(name = "sent_to_provider_at")
    private LocalDateTime sentToProviderAt;

    @Column(name = "received_from_provider_at")
    private LocalDateTime receivedFromProviderAt;

    @Column(name = "delivered_to_buyer_at")
    private LocalDateTime deliveredToBuyerAt;

    @Column(name = "provider_error_code", length = 50)
    private String providerErrorCode;

    @Column(name = "provider_error_message", columnDefinition = "TEXT")
    private String providerErrorMessage;

    // --- System Audit Shell Fields ---
    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private EinvMerchantUserEntity deletedByUser;

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