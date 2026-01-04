package com.einvoicehub.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Invoice Response DTO - Unified Invoice Response Model
 *
 * Model chuẩn hóa cho response từ các Provider
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
