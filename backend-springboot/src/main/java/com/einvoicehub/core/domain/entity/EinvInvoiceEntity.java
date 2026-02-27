package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Hóa đơn điện tử */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_invoices",
        uniqueConstraints = { @UniqueConstraint(name = "uq_biz_invoice", columnNames = {"store_id", "partner_invoice_id"}) },
        indexes = {
                @Index(name = "idx_inv_lookup_code", columnList = "invoice_lookup_code"),
                @Index(name = "idx_inv_status", columnList = "status_id"),
                @Index(name = "idx_inv_partner_inv", columnList = "partner_invoice_id")
        })
public class EinvInvoiceEntity extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", length = 36, nullable = false)
    private String storeId;

    @Column(name = "provider_id", length = 36)
    private String providerId;

    @Column(name = "partner_invoice_id", length = 50, nullable = false)
    private String partnerInvoiceId; // ID từ POS

    @Column(name = "provider_invoice_id", length = 50)
    private String providerInvoiceId; // ID do NCC cấp

    @Column(name = "provider_response_id", length = 100)
    private String providerResponseId;

    @Builder.Default
    @Column(name = "status_id", nullable = false)
    private Integer statusId = 0;

    @Column(name = "tax_status_id")
    private Integer taxStatusId = 0;

    @Column(name = "cqt_response_code", length = 10)
    private String cqtResponseCode;

    @Column(name = "is_draft")
    private Boolean isDraft = false;

    @Column(name = "is_mtt")
    private Boolean isMtt = false; // Máy tính tiền

    @Column(name = "is_petrol")
    private Boolean isPetrol = false; // Xăng dầu

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Builder.Default
    @Column(name = "is_locked")
    private Boolean isLocked = false;

    // Phân loại
    @Column(name = "invoice_type_id")
    private Integer invoiceTypeId;

    @Column(name = "reference_type_id", nullable = false)
    private Integer referenceTypeId = 0;

    @Column(name = "sign_type")
    private Integer signType;

    @Column(name = "payment_method_id")
    private Integer paymentMethodId;

    @Column(name = "invoice_form", length = 50)
    private String invoiceForm;

    @Column(name = "invoice_series", length = 20)
    private String invoiceSeries;

    @Column(name = "invoice_no", length = 8)
    private String invoiceNo;

    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column(name = "signed_date")
    private LocalDateTime signedDate;

    @Lob
    @Column(name = "hash_value")
    private String hashValue;

    @Column(name = "tax_authority_code", length = 100)
    private String taxAuthorityCode;

    @Column(name = "invoice_lookup_code", length = 50)
    private String invoiceLookupCode;

    @Column(name = "response_message", length = 500)
    private String responseMessage;

    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Column(name = "secret_code", length = 50)
    private String secretCode;

    // Thông tin người mua
    @Column(name = "buyer_tax_code", length = 50)
    private String buyerTaxCode;

    @Column(name = "buyer_code", length = 50)
    private String buyerCode;

    @Column(name = "buyer_unit_name", length = 300)
    private String buyerUnitName;

    @Column(name = "buyer_name", length = 200)
    private String buyerName;

    @Column(name = "buyer_address", length = 300)
    private String buyerAddress;

    @Column(name = "buyer_email", length = 100)
    private String buyerEmail;

    @Column(name = "buyer_id_no", length = 20)
    private String buyerIdNo;

    @Column(name = "buyer_bank_account", length = 20)
    private String buyerBankAccount;

    @Column(name = "buyer_bank_name", length = 100)
    private String buyerBankName;

    @Column(name = "buyer_budget_code", length = 20)
    private String buyerBudgetCode;

    @Column(name = "buyer_mobile", length = 50)
    private String buyerMobile;

    @Column(name = "receive_type_id")
    private Integer receiveTypeId;

    @Column(name = "receiver_email", length = 50)
    private String receiverEmail;

    @Column(name = "buyer_plate_no", length = 50)
    private String buyerPlateNo;

    @Column(name = "extra_metadata", columnDefinition = "json")
    private String extraMetaData;

    @Column(name = "delivery_info", columnDefinition = "json")
    private String deliveryInfo;

    @Column(name = "tax_summary_json", columnDefinition = "json")
    private String taxSummaryJson;

    // Tiền tệ
    @Column(name = "currency_code", length = 20)
    private String currencyCode = "VND";

    @Column(name = "exchange_rate", precision = 18, scale = 6)
    private BigDecimal exchangeRate = BigDecimal.ONE;

    @Column(name = "gross_amount", precision = 20, scale = 2)
    private BigDecimal grossAmount;

    @Column(name = "discount_amount", precision = 20, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "net_amount", precision = 20, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "tax_amount", precision = 20, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", precision = 20, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "total_amount_text", length = 500)
    private String totalAmountText;

    @Column(name = "notes", length = 300)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_invoice_id", foreignKey = @ForeignKey(name = "fk_inv_org_ref"))
    private EinvInvoiceEntity originalInvoice;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submit_invoice_type", insertable = false, updatable = false)
    private EinvSubmitInvoiceTypeEntity submitInvoiceTypeRef;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EinvInvoiceDetailEntity> details = new ArrayList<>();

    @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL)
    private EinvInvoicePayloadEntity payload;

    public void addDetail(EinvInvoiceDetailEntity detail) {
        details.add(detail);
        detail.setInvoice(this);
    }
}