package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "einv_invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = {"merchant", "store", "provider", "details"})
public class EinvInvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private EinvMerchantEntity merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private EinvStoreEntity store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private EinvProviderEntity provider;

    @Column(name = "partner_invoice_id", length = 50, nullable = false)
    private String partnerInvoiceId; //ID POS

    @Column(name = "provider_invoice_id", length = 50)
    private String providerInvoiceId; //ID do NCC cấp trả về

    @Column(name = "invoice_type_id")
    private Integer invoiceTypeId;

    @Column(name = "reference_type_id", nullable = false)
    private Integer referenceTypeId; // 0=Gốc | 1=Điều chỉnh | 2=Thay thế

    @Column(name = "payment_method_id")
    private Integer paymentMethodId;

    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "invoice_form", length = 50)
    private String invoiceForm;

    @Column(name = "invoice_series", length = 20)
    private String invoiceSeries;

    @Column(name = "invoice_no", length = 20)
    private String invoiceNo;

    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column(name = "signed_date")
    private LocalDateTime signedDate;

    @Column(name = "tax_authority_code", length = 100)
    private String taxAuthorityCode; // Mã CQT cấp

    @Column(name = "invoice_lookup_code", length = 50)
    private String invoiceLookupCode;

    @Column(name = "buyer_tax_code", length = 50)
    private String buyerTaxCode;

    @Column(name = "buyer_company", length = 300)
    private String buyerCompany;

    @Column(name = "buyer_id_no", length = 20)
    private String buyerIdNo;

    @Column(name = "buyer_full_name", length = 200)
    private String buyerFullName;

    @Column(name = "buyer_address", length = 300)
    private String buyerAddress;

    @Column(name = "buyer_mobile", length = 50)
    private String buyerMobile;

    @Column(name = "buyer_bank_account", length = 50)
    private String buyerBankAccount;

    @Column(name = "buyer_bank_name", length = 200)
    private String buyerBankName;

    @Column(name = "buyer_budget_code", length = 20)
    private String buyerBudgetCode;

    @Column(name = "receive_type_id")
    private Integer receiveTypeId;

    @Column(name = "receiver_email", length = 300)
    private String receiverEmail;

    @Column(name = "currency_code", length = 20)
    private String currencyCode;

    @Column(name = "exchange_rate", precision = 15, scale = 6)
    private BigDecimal exchangeRate;

    @Column(name = "gross_amount", precision = 15, scale = 2)
    private BigDecimal grossAmount;

    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "net_amount", precision = 15, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    // --- Thông tin hóa đơn gốc (Adjustment/Replace) ---
    @Column(name = "org_invoice_id", length = 36)
    private String orgInvoiceId;

    @Column(name = "org_invoice_form", length = 50)
    private String orgInvoiceForm;

    @Column(name = "org_invoice_series", length = 20)
    private String orgInvoiceSeries;

    @Column(name = "org_invoice_no", length = 50)
    private String orgInvoiceNo;

    @Column(name = "org_invoice_date")
    private LocalDateTime orgInvoiceDate;

    @Column(name = "org_invoice_reason", length = 500)
    private String orgInvoiceReason;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<EinvInvoiceDetailEntity> details;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.referenceTypeId == null) this.referenceTypeId = 0;
        if (this.statusId == null) this.statusId = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}