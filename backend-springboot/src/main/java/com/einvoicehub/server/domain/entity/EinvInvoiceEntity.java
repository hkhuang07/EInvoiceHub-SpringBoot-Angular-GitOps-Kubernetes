package com.einvoicehub.server.domain.entity;

import com.einvoicehub.server.config.JsonConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "einv_invoices",
        indexes = {
                @Index(name = "idx_inv_tenant", columnList = "tenant_id"),
                @Index(name = "idx_inv_status", columnList = "status_id"),
                @Index(name = "idx_inv_date", columnList = "invoice_date"),
                @Index(name = "idx_inv_lookup_code", columnList = "invoice_lookup_code"),
                @Index(name = "idx_inv_buyer_tax", columnList = "buyer_tax_code"),
                @Index(name = "idx_inv_tenant_partner", columnList = "tenant_id, partner_invoice_id"),
                @Index(name = "idx_inv_provider_date", columnList = "provider_id, invoice_date"),
                @Index(name = "idx_inv_tenant_status", columnList = "tenant_id, status_id"),
                @Index(name = "idx_inv_store_status", columnList = "store_id, status_id"),
                @Index(name = "idx_inv_provider_status", columnList = "provider_id, status_id"),
                @Index(name = "idx_inv_created_date", columnList = "created_date")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_inv_tenant_partner", columnNames = {"tenant_id", "partner_invoice_id"})
        })
public class EinvInvoiceEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    // Denormalized for future sharding/partitioning by tenant
    @Column(name = "tenant_id", length = 36, nullable = true)
    private String tenantId;

    @Column(name = "store_id", length = 36)
    private String storeId;

    @Column(name = "provider_id", length = 36)
    private String providerId;

    @Column(name = "partner_invoice_id", length = 50)
    private String partnerInvoiceId;

    @Column(name = "provider_invoice_id", length = 50)
    private String providerInvoiceId;

    // TINYINT in schema
    @Column(name = "invoice_type_id", columnDefinition = "TINYINT")
    private Byte invoiceTypeId;

    // TINYINT in schema (0: Gốc, 2: Điều chỉnh, 3: Thay thế)
    @Column(name = "reference_type_id", columnDefinition = "TINYINT")
    private Byte referenceTypeId;

    // TINYINT in schema
    @Column(name = "status_id", columnDefinition = "TINYINT")
    private Byte statusId;

    @Column(name = "invoice_form", length = 50)
    private String invoiceForm;

    @Column(name = "invoice_series", length = 50)
    private String invoiceSeries;

    @Column(name = "invoice_no", length = 50)
    private String invoiceNo;

    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column(name = "signed_date")
    private LocalDateTime signedDate;

    // TINYINT in schema
    @Column(name = "payment_method_id", columnDefinition = "TINYINT")
    private Byte paymentMethodId;

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

    // TINYINT in schema
    @Column(name = "receive_type_id", columnDefinition = "TINYINT")
    private Byte receiveTypeId;

    @Column(name = "receiver_email", length = 300)
    private String receiverEmail;

    @Column(name = "currency_code", length = 20)
    private String currencyCode = "VND";

    @Column(name = "exchange_rate", precision = 10, scale = 2)
    private BigDecimal exchangeRate = BigDecimal.ONE;

    @Column(name = "tax_authority_code", length = 50)
    private String taxAuthorityCode;

    @Column(name = "invoice_lookup_code", length = 50)
    private String invoiceLookupCode;

    @Column(name = "org_invoice_id", length = 36)
    private String orgInvoiceId;

    @Column(name = "org_invoice_form", length = 50)
    private String orgInvoiceForm;

    @Column(name = "org_invoice_series", length = 50)
    private String orgInvoiceSeries;

    @Column(name = "org_invoice_no", length = 50)
    private String orgInvoiceNo;

    @Column(name = "org_invoice_date")
    private LocalDateTime orgInvoiceDate;

    @Column(name = "org_invoice_reason", length = 500)
    private String orgInvoiceReason;

    // Thành tiền hàng hóa (trước chiết khấu)
    @Column(name = "gross_amount", precision = 15, scale = 2)
    private BigDecimal grossAmount;

    // Số tiền chiết khấu
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;

    // Thành tiền trước thuế (sau chiết khấu)
    @Column(name = "net_amount", precision = 15, scale = 2)
    private BigDecimal netAmount;

    // Số tiền thuế
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;

    // Trị giá thanh toán (tổng cộng)
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    // TINYINT in schema, default 0
    @Column(name = "tax_status_id", columnDefinition = "TINYINT DEFAULT 0")
    private Byte taxStatusId = 0;

    @Column(name = "cqt_response_code", length = 10)
    private String cqtResponseCode;

    @Column(name = "provider_response_id", length = 100)
    private String providerResponseId;

    // TINYINT(1) in schema
    @Column(name = "is_draft", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDraft = false;

    // TINYINT(1) in schema
    @Column(name = "is_mtt", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isMtt = false;

    // TINYINT(1) in schema
    @Column(name = "is_petrol", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isPetrol = false;

    // TINYINT(1) in schema
    @Column(name = "is_locked", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isLocked = false;

    // xóa mềm - có thể khôi phục - TINYINT(1) in schema
    @Column(name = "is_deleted", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted = false;

    // Loại ký: 0 = Token, 1 = HSM - TINYINT in schema
    @Column(name = "sign_type", columnDefinition = "TINYINT")
    private Byte signType;

    @Column(name = "submit_invoice_type", length = 3)
    private String submitInvoiceType;

    // Thông báo từ NCC
    @Column(name = "response_message", length = 500)
    private String responseMessage;

    // Mã lỗi từ NCC
    @Column(name = "error_code", length = 50)
    private String errorCode;

    // Mã bí mật (Viettel)
    @Column(name = "secret_code", length = 50)
    private String secretCode;

    // Biển số xe
    @Column(name = "buyer_plate_no", length = 50)
    private String buyerPlateNo;

    // Trường động (MISA/VNPT) - JSON
    @Column(name = "extra_metadata", columnDefinition = "JSON")
    @Convert(converter = JsonConverter.class)
    private Object extraMetadata;

    @Column(name = "delivery_info", columnDefinition = "JSON")
    @Convert(converter = JsonConverter.class)
    private Object deliveryInfo;

    @Column(name = "total_amount_text", length = 500)
    private String totalAmountText;

    // tổng hợp thuế suất cho XML CQT - JSON
    @Column(name = "tax_summary_json", columnDefinition = "JSON")
    @Convert(converter = JsonConverter.class)
    private Object taxSummaryJson;

    @Column(name = "notes", length = 300)
    private String notes;

    // Danh sách chi tiết hóa đơn (One-to-Many)
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EinvInvoiceDetailEntity> details = new ArrayList<>();

    // Getters and Setters

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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getPartnerInvoiceId() {
        return partnerInvoiceId;
    }

    public void setPartnerInvoiceId(String partnerInvoiceId) {
        this.partnerInvoiceId = partnerInvoiceId;
    }

    public String getProviderInvoiceId() {
        return providerInvoiceId;
    }

    public void setProviderInvoiceId(String providerInvoiceId) {
        this.providerInvoiceId = providerInvoiceId;
    }

    public Byte getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(Byte invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    public Byte getReferenceTypeId() {
        return referenceTypeId;
    }

    public void setReferenceTypeId(Byte referenceTypeId) {
        this.referenceTypeId = referenceTypeId;
    }

    public Byte getStatusId() {
        return statusId;
    }

    public void setStatusId(Byte statusId) {
        this.statusId = statusId;
    }

    public String getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(String invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDateTime getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(LocalDateTime signedDate) {
        this.signedDate = signedDate;
    }

    public Byte getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Byte paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getBuyerTaxCode() {
        return buyerTaxCode;
    }

    public void setBuyerTaxCode(String buyerTaxCode) {
        this.buyerTaxCode = buyerTaxCode;
    }

    public String getBuyerCompany() {
        return buyerCompany;
    }

    public void setBuyerCompany(String buyerCompany) {
        this.buyerCompany = buyerCompany;
    }

    public String getBuyerIdNo() {
        return buyerIdNo;
    }

    public void setBuyerIdNo(String buyerIdNo) {
        this.buyerIdNo = buyerIdNo;
    }

    public String getBuyerFullName() {
        return buyerFullName;
    }

    public void setBuyerFullName(String buyerFullName) {
        this.buyerFullName = buyerFullName;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerMobile() {
        return buyerMobile;
    }

    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
    }

    public String getBuyerBankAccount() {
        return buyerBankAccount;
    }

    public void setBuyerBankAccount(String buyerBankAccount) {
        this.buyerBankAccount = buyerBankAccount;
    }

    public String getBuyerBankName() {
        return buyerBankName;
    }

    public void setBuyerBankName(String buyerBankName) {
        this.buyerBankName = buyerBankName;
    }

    public String getBuyerBudgetCode() {
        return buyerBudgetCode;
    }

    public void setBuyerBudgetCode(String buyerBudgetCode) {
        this.buyerBudgetCode = buyerBudgetCode;
    }

    public Byte getReceiveTypeId() {
        return receiveTypeId;
    }

    public void setReceiveTypeId(Byte receiveTypeId) {
        this.receiveTypeId = receiveTypeId;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getTaxAuthorityCode() {
        return taxAuthorityCode;
    }

    public void setTaxAuthorityCode(String taxAuthorityCode) {
        this.taxAuthorityCode = taxAuthorityCode;
    }

    public String getInvoiceLookupCode() {
        return invoiceLookupCode;
    }

    public void setInvoiceLookupCode(String invoiceLookupCode) {
        this.invoiceLookupCode = invoiceLookupCode;
    }

    public String getOrgInvoiceId() {
        return orgInvoiceId;
    }

    public void setOrgInvoiceId(String orgInvoiceId) {
        this.orgInvoiceId = orgInvoiceId;
    }

    public String getOrgInvoiceForm() {
        return orgInvoiceForm;
    }

    public void setOrgInvoiceForm(String orgInvoiceForm) {
        this.orgInvoiceForm = orgInvoiceForm;
    }

    public String getOrgInvoiceSeries() {
        return orgInvoiceSeries;
    }

    public void setOrgInvoiceSeries(String orgInvoiceSeries) {
        this.orgInvoiceSeries = orgInvoiceSeries;
    }

    public String getOrgInvoiceNo() {
        return orgInvoiceNo;
    }

    public void setOrgInvoiceNo(String orgInvoiceNo) {
        this.orgInvoiceNo = orgInvoiceNo;
    }

    public LocalDateTime getOrgInvoiceDate() {
        return orgInvoiceDate;
    }

    public void setOrgInvoiceDate(LocalDateTime orgInvoiceDate) {
        this.orgInvoiceDate = orgInvoiceDate;
    }

    public String getOrgInvoiceReason() {
        return orgInvoiceReason;
    }

    public void setOrgInvoiceReason(String orgInvoiceReason) {
        this.orgInvoiceReason = orgInvoiceReason;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Byte getTaxStatusId() {
        return taxStatusId;
    }

    public void setTaxStatusId(Byte taxStatusId) {
        this.taxStatusId = taxStatusId;
    }

    public String getCqtResponseCode() {
        return cqtResponseCode;
    }

    public void setCqtResponseCode(String cqtResponseCode) {
        this.cqtResponseCode = cqtResponseCode;
    }

    public String getProviderResponseId() {
        return providerResponseId;
    }

    public void setProviderResponseId(String providerResponseId) {
        this.providerResponseId = providerResponseId;
    }

    public Boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    public Boolean getIsMtt() {
        return isMtt;
    }

    public void setIsMtt(Boolean isMtt) {
        this.isMtt = isMtt;
    }

    public Boolean getIsPetrol() {
        return isPetrol;
    }

    public void setIsPetrol(Boolean isPetrol) {
        this.isPetrol = isPetrol;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Byte getSignType() {
        return signType;
    }

    public void setSignType(Byte signType) {
        this.signType = signType;
    }

    public String getSubmitInvoiceType() {
        return submitInvoiceType;
    }

    public void setSubmitInvoiceType(String submitInvoiceType) {
        this.submitInvoiceType = submitInvoiceType;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public String getBuyerPlateNo() {
        return buyerPlateNo;
    }

    public void setBuyerPlateNo(String buyerPlateNo) {
        this.buyerPlateNo = buyerPlateNo;
    }

    public Object getExtraMetadata() {
        return extraMetadata;
    }

    public void setExtraMetadata(Object extraMetadata) {
        this.extraMetadata = extraMetadata;
    }

    public Object getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(Object deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public String getTotalAmountText() {
        return totalAmountText;
    }

    public void setTotalAmountText(String totalAmountText) {
        this.totalAmountText = totalAmountText;
    }

    public Object getTaxSummaryJson() {
        return taxSummaryJson;
    }

    public void setTaxSummaryJson(Object taxSummaryJson) {
        this.taxSummaryJson = taxSummaryJson;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<EinvInvoiceDetailEntity> getDetails() {
        return details;
    }

    public void setDetails(List<EinvInvoiceDetailEntity> details) {
        this.details = details;
    }

    // Helper method to add invoice detail
    public void addDetail(EinvInvoiceDetailEntity detail) {
        details.add(detail);
        detail.setInvoice(this);
    }

    // Helper method to remove invoice detail
    public void removeDetail(EinvInvoiceDetailEntity detail) {
        details.remove(detail);
        detail.setInvoice(null);
    }
}
