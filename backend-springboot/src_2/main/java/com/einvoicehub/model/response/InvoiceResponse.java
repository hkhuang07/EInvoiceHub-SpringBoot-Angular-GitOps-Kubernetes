package com.einvoicehub.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Invoice Response DTO - Unified Invoice Response Model
 * 
 * Model chuẩn hóa cho response từ các Provider
 */
public class InvoiceResponse {

    private boolean success;
    private String errorCode;
    private String errorMessage;
    private String providerCode;
    
    // Provider transaction ID
    private String transactionId;
    private String clientRequestId;
    
    // Invoice information
    private String invoiceNumber;
    private String invoiceCode;
    private String invoicePattern;
    private String invoiceSerial;
    
    private String status;
    private String statusDescription;
    
    private LocalDateTime issuedDate;
    private LocalDateTime signedDate;
    
    // Financial information
    private BigDecimal totalAmount;
    private BigDecimal totalTaxAmount;
    private BigDecimal grandTotalAmount;
    private String currency;
    
    // Buyer information
    private String buyerName;
    private String buyerTaxCode;
    
    // Seller information
    private String sellerName;
    private String sellerTaxCode;
    
    // Download links
    private String pdfDownloadUrl;
    private String xmlDownloadUrl;
    private String htmlDownloadUrl;
    
    // Additional data
    private String rawResponse;
    private String providerMessage;

    // Constructors
    public InvoiceResponse() {
    }

    public InvoiceResponse(boolean success) {
        this.success = success;
    }

    // Builder pattern for easier construction
    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getClientRequestId() {
        return clientRequestId;
    }

    public void setClientRequestId(String clientRequestId) {
        this.clientRequestId = clientRequestId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
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

    public String getPdfDownloadUrl() {
        return pdfDownloadUrl;
    }

    public void setPdfDownloadUrl(String pdfDownloadUrl) {
        this.pdfDownloadUrl = pdfDownloadUrl;
    }

    public String getXmlDownloadUrl() {
        return xmlDownloadUrl;
    }

    public void setXmlDownloadUrl(String xmlDownloadUrl) {
        this.xmlDownloadUrl = xmlDownloadUrl;
    }

    public String getHtmlDownloadUrl() {
        return htmlDownloadUrl;
    }

    public void setHtmlDownloadUrl(String htmlDownloadUrl) {
        this.htmlDownloadUrl = htmlDownloadUrl;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public String getProviderMessage() {
        return providerMessage;
    }

    public void setProviderMessage(String providerMessage) {
        this.providerMessage = providerMessage;
    }

    // Builder class
    public static class Builder {
        private final InvoiceResponse response = new InvoiceResponse();

        public Builder success(boolean success) {
            response.success = success;
            return this;
        }

        public Builder errorCode(String errorCode) {
            response.errorCode = errorCode;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            response.errorMessage = errorMessage;
            return this;
        }

        public Builder providerCode(String providerCode) {
            response.providerCode = providerCode;
            return this;
        }

        public Builder transactionId(String transactionId) {
            response.transactionId = transactionId;
            return this;
        }

        public Builder clientRequestId(String clientRequestId) {
            response.clientRequestId = clientRequestId;
            return this;
        }

        public Builder invoiceNumber(String invoiceNumber) {
            response.invoiceNumber = invoiceNumber;
            return this;
        }

        public Builder status(String status) {
            response.status = status;
            return this;
        }

        public Builder totalAmount(BigDecimal totalAmount) {
            response.totalAmount = totalAmount;
            return this;
        }

        public Builder grandTotalAmount(BigDecimal grandTotalAmount) {
            response.grandTotalAmount = grandTotalAmount;
            return this;
        }

        public Builder buyerName(String buyerName) {
            response.buyerName = buyerName;
            return this;
        }

        public Builder buyerTaxCode(String buyerTaxCode) {
            response.buyerTaxCode = buyerTaxCode;
            return this;
        }

        public Builder sellerName(String sellerName) {
            response.sellerName = sellerName;
            return this;
        }

        public Builder sellerTaxCode(String sellerTaxCode) {
            response.sellerTaxCode = sellerTaxCode;
            return this;
        }

        public Builder rawResponse(String rawResponse) {
            response.rawResponse = rawResponse;
            return this;
        }

        public InvoiceResponse build() {
            return response;
        }
    }
}