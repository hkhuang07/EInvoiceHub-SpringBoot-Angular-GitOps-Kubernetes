package com.einvoicehub.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Invoice Metadata Entity - Lưu trữ metadata của hóa đơn
 * 
 * Lưu trữ thông tin metadata của hóa đơn trong MySQL
 * Dữ liệu payload chi tiết được lưu trong MongoDB
 */
@Entity
@Table(name = "invoice_metadata", indexes = {
    @Index(name = "idx_merchant_id", columnList = "merchant_id"),
    @Index(name = "idx_client_request_id", columnList = "client_request_id"),
    @Index(name = "idx_provider_code", columnList = "provider_code"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_invoice_number", columnList = "invoice_number"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
public class InvoiceMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "client_request_id", unique = true, length = 100)
    private String clientRequestId;

    @Column(name = "provider_code", nullable = false, length = 50)
    private String providerCode;

    // Provider transaction ID
    @Column(name = "provider_transaction_id", length = 100)
    private String providerTransactionId;

    // Invoice details from provider
    @Column(name = "invoice_number", length = 50)
    private String invoiceNumber;

    @Column(name = "invoice_code", length = 50)
    private String invoiceCode;

    @Column(name = "invoice_pattern", length = 50)
    private String invoicePattern;

    @Column(name = "invoice_serial", length = 50)
    private String invoiceSerial;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    @Column(name = "status_message", length = 500)
    private String statusMessage;

    // Financial summary
    @Column(name = "total_amount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "total_tax_amount", precision = 18, scale = 2)
    private BigDecimal totalTaxAmount;

    @Column(name = "grand_total_amount", precision = 18, scale = 2)
    private BigDecimal grandTotalAmount;

    @Column(name = "currency", length = 10)
    private String currency = "VND";

    // Party information
    @Column(name = "seller_name", length = 255)
    private String sellerName;

    @Column(name = "seller_tax_code", length = 20)
    private String sellerTaxCode;

    @Column(name = "buyer_name", length = 255)
    private String buyerName;

    @Column(name = "buyer_tax_code", length = 20)
    private String buyerTaxCode;

    // MongoDB document reference
    @Column(name = "payload_id", length = 50)
    private String payloadId;

    // Timestamps
    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column(name = "issued_date")
    private LocalDateTime issuedDate;

    @Column(name = "signed_date")
    private LocalDateTime signedDate;

    @Column(name = "cancelled_date")
    private LocalDateTime cancelledDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Error tracking
    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    // Download URLs
    @Column(name = "pdf_url", length = 500)
    private String pdfUrl;

    @Column(name = "xml_url", length = 500)
    private String xmlUrl;

    @Column(name = "html_url", length = 500)
    private String htmlUrl;

    // Audit fields
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getClientRequestId() {
        return clientRequestId;
    }

    public void setClientRequestId(String clientRequestId) {
        this.clientRequestId = clientRequestId;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getProviderTransactionId() {
        return providerTransactionId;
    }

    public void setProviderTransactionId(String providerTransactionId) {
        this.providerTransactionId = providerTransactionId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoicePattern() {
        return invoicePattern;
    }

    public void setInvoicePattern(String invoicePattern) {
        this.invoicePattern = invoicePattern;
    }

    public String getInvoiceSerial() {
        return invoiceSerial;
    }

    public void setInvoiceSerial(String invoiceSerial) {
        this.invoiceSerial = invoiceSerial;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getGrandTotalAmount() {
        return grandTotalAmount;
    }

    public void setGrandTotalAmount(BigDecimal grandTotalAmount) {
        this.grandTotalAmount = grandTotalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerTaxCode() {
        return sellerTaxCode;
    }

    public void setSellerTaxCode(String sellerTaxCode) {
        this.sellerTaxCode = sellerTaxCode;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerTaxCode() {
        return buyerTaxCode;
    }

    public void setBuyerTaxCode(String buyerTaxCode) {
        this.buyerTaxCode = buyerTaxCode;
    }

    public String getPayloadId() {
        return payloadId;
    }

    public void setPayloadId(String payloadId) {
        this.payloadId = payloadId;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDateTime getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(LocalDateTime signedDate) {
        this.signedDate = signedDate;
    }

    public LocalDateTime getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(LocalDateTime cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getXmlUrl() {
        return xmlUrl;
    }

    public void setXmlUrl(String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // Business methods
    public void incrementRetryCount() {
        this.retryCount++;
    }

    public void markAsIssued() {
        this.status = InvoiceStatus.ISSUED;
        this.issuedDate = LocalDateTime.now();
        this.completedAt = LocalDateTime.now();
    }

    public void markAsFailed(String errorCode, String errorMessage) {
        this.status = InvoiceStatus.FAILED;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public void markAsCancelled() {
        this.status = InvoiceStatus.CANCELLED;
        this.cancelledDate = LocalDateTime.now();
        this.completedAt = LocalDateTime.now();
    }
}