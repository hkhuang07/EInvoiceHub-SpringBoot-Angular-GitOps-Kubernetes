package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.softz.core.entity.BaseStoreEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "einv_invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EinvHubInvoiceEntity extends BaseStoreEntity {

    @Column(name = "partner_invoice_id", length = 50)
    private String partnerInvoiceId;

    @Column(name = "provider_id", length = 36)
    private String providerId;

    @Column(name = "provider_invoice_id", length = 50)
    private String providerInvoiceId;

    @Column(name = "invoice_type_id")
    private Integer invoiceTypeId;

    @Column(name = "reference_type_id")
    private Integer referenceTypeId;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "invoice_form", length = 50)
    private String invoiceForm;

    @Column(name = "invoice_series", length = 50)
    private String invoiceSeries;

    @Column(name = "invoice_no", length = 50)
    private String invoiceNo;

    @Column(name = "invoice_date")
    private Instant invoiceDate;

    @Column(name = "payment_method_id")
    private Integer paymentMethodId;

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

    @Column(name = "exchange_rate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "signed_date")
    private Instant signedDate;

    @Column(name = "tax_authority_code", length = 50)
    private String taxAuthorityCode;

    @Column(name = "org_invoice_id", length = 36)
    private String orgInvoiceId;

    @Column(name = "org_invoice_form", length = 50)
    private String orgInvoiceForm;

    @Column(name = "org_invoice_series", length = 50)
    private String orgInvoiceSeries;

    @Column(name = "org_invoice_no", length = 50)
    private String orgInvoiceNo;

    @Column(name = "org_invoice_date")
    private Instant orgInvoiceDate;

    @Column(name = "org_invoice_reason", length = 500)
    private String orgInvoiceReason;

    @Column(name = "invoice_lookup_code", length = 50)
    private String invoiceLookupCode;

    // Amounts
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

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EinvHubInvoiceDetailEntity> details;
}
