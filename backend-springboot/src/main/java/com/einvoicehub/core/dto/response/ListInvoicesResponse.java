package com.einvoicehub.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListInvoicesResponse {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;

    @JsonProperty("MerchantName")
    private String merchantName;

    @JsonProperty("InvoiceNumber")
    private String invoiceNumber;

    @JsonProperty("SymbolCode")
    private String symbolCode;

    @JsonProperty("IssueDate")
    private String issueDate;

    @JsonProperty("BuyerName")
    private String buyerName;

    @JsonProperty("BuyerTaxCode")
    private String buyerTaxCode;

    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("CurrencyCode")
    private String currencyCode;

    @JsonProperty("StatusName")
    private String statusName;

    @JsonProperty("PaymentMethodName")
    private String paymentMethodName;

    @JsonProperty("TaxAuthorityCode")
    private String taxAuthorityCode;

    @JsonProperty("CreatedAt")
    private String createdAt;

    @JsonProperty("CreatedByFullName")
    private String createdByFullName;
}