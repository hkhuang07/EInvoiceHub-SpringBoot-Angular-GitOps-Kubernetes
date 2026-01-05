package com.einvoicehub.core.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {

    private boolean success;
    private String errorCode;
    private String message;
    private String providerCode;

    private String transactionId;
    private String clientRequestId;

    private String invoiceNumber;
    private String invoiceCode;
    private String invoiceSerial;

    private String status;
    private String statusDescription;

    private LocalDateTime issuedDate;
    private LocalDateTime signedDate;

    private BigDecimal totalAmount;
    private BigDecimal totalTaxAmount;
    private BigDecimal grandTotalAmount;
    private String currency;

    private String buyerName;
    private String buyerTaxCode;
    private String sellerName;
    private String sellerTaxCode;

    private String pdfDownloadUrl;
    private String xmlDownloadUrl;

    private String rawResponse;
    private String providerMessage;
}