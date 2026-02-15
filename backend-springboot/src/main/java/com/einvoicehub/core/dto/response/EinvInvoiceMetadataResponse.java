package com.einvoicehub.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import com.einvoicehub.core.dto.EinvInvoiceItemDto;
import com.einvoicehub.core.dto.EinvInvoiceTaxBreakdownDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceMetadataResponse {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("MerchantID")
    private Long merchantId;

    @JsonProperty("MerchantName")
    private String merchantName;

    @JsonProperty("StatusID")
    private Integer statusId;

    @JsonProperty("StatusName")
    private String statusName;

    @JsonProperty("StatusMessage")
    private String statusMessage;

    @JsonProperty("InvoiceNumber")
    private String invoiceNumber;

    @JsonProperty("SymbolCode")
    private String symbolCode;

    @JsonProperty("TemplateCode")
    private String templateCode;

    @JsonProperty("LookupCode")
    private String lookupCode;

    @JsonProperty("TaxAuthorityCode")
    private String taxAuthorityCode; // cqt_code trong SQL

    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;

    @JsonProperty("PaymentMethod")
    private String paymentMethod;

    @JsonProperty("SellerName")
    private String sellerName;

    @JsonProperty("SellerTaxCode")
    private String sellerTaxCode;

    @JsonProperty("SellerAddress")
    private String sellerAddress;

    @JsonProperty("BuyerName")
    private String buyerName;

    @JsonProperty("BuyerTaxCode")
    private String buyerTaxCode;

    @JsonProperty("BuyerIdNo")
    private String buyerIdNo;

    @JsonProperty("BuyerFullName")
    private String buyerFullName;

    @JsonProperty("BuyerEmail")
    private String buyerEmail;

    @JsonProperty("BuyerPhone")
    private String buyerPhone;

    @JsonProperty("BuyerAddress")
    private String buyerAddress;

    @JsonProperty("BuyerBankAccount")
    private String buyerBankAccount;

    @JsonProperty("BuyerBankName")
    private String buyerBankName;

    @JsonProperty("SubtotalAmount")
    private BigDecimal subtotalAmount;

    @JsonProperty("TaxAmount")
    private BigDecimal taxAmount;

    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;

    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("CurrencyCode")
    private String currencyCode;

    @JsonProperty("ExchangeRate")
    private BigDecimal exchangeRate;

    @JsonProperty("IssueDate")
    private String issueDate;

    @JsonProperty("SignedAt")
    private String signedAt;

    @JsonProperty("CreatedAt")
    private String createdAt;

    @JsonProperty("Items")
    private List<EinvInvoiceItemDto> items;

    @JsonProperty("TaxBreakdowns")
    private List<EinvInvoiceTaxBreakdownDto> taxBreakdowns;
}