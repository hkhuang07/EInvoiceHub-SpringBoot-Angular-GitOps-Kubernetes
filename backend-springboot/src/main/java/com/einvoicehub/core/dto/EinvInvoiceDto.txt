package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceDto {

    @JsonProperty("InvoiceID")
    private String id;

    @JsonProperty("TenantID")
    private Long tenantId;

    @JsonProperty("StoreID")
    private String storeId;

    @JsonProperty("Provider")
    private String providerId;

    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;

    @JsonProperty("ProviderInvoiceID")
    private String providerInvoiceId;

    @JsonProperty("ProviderResponseID")
    private String providerResponseId;

    @JsonProperty("InvoiceStatusID")
    private Integer statusId;

    @JsonProperty("TaxStatusID")
    private Integer taxStatusId;

    @JsonProperty("CqtResponseCode")
    private String cqtResponseCode;

    @JsonProperty("IsDraft")
    private Boolean isDraft;

    @JsonProperty("IsMtt")
    private Boolean isMtt;

    @JsonProperty("IsPetrol")
    private Boolean isPetrol;

    @JsonProperty("IsLocked")
    private Boolean isLocked;

    @JsonProperty("IsDeleted")
    private Boolean isDeleted;

    @JsonProperty("InvoiceTypeID")
    private Integer invoiceTypeId;

    @JsonProperty("ReferenceTypeID")
    private Integer referenceTypeId;

    @JsonProperty("SignType")
    private Integer signType;

    @JsonProperty("PaymentMethodID")
    private Integer paymentMethodId;

    @JsonProperty("InvoiceForm")
    private String invoiceForm;

    @JsonProperty("InvoiceSeries")
    private String invoiceSeries;

    @JsonProperty("InvoiceNo")
    private String invoiceNo;

    @JsonProperty("InvoiceDate")
    private LocalDateTime invoiceDate;

    @JsonProperty("SignedDate")
    private LocalDateTime signedDate;

    @JsonProperty("HashValue")
    private String hashValue;

    @JsonProperty("TaxAuthorityCode")
    private String taxAuthorityCode;

    @JsonProperty("InvoiceLookupCode")
    private String invoiceLookupCode;

    @JsonProperty("UrlLookup")
    private String urlLookup;

    @JsonProperty("ResponseMessage")
    private String responseMessage;

    @JsonProperty("SecretCode")
    private String secretCode;

    @JsonProperty("BuyerIDNo")
    private String buyerIdNo;

    @JsonProperty("BuyerBankAccount")
    private String buyerBankAccount;

    @JsonProperty("BuyerBankName")
    private String buyerBankName;

    @JsonProperty("BuyerBudgetCode")
    private String buyerBudgetCode;

    @JsonProperty("BuyerTaxCode")
    private String buyerTaxCode;

    @JsonProperty("BuyerCode")
    private String buyerCode;

    @JsonProperty("BuyerUnitName")
    private String buyerUnitName;

    @JsonProperty("BuyerName")
    private String BuyerName;

    @JsonProperty("BuyerAddress")
    private String buyerAddress;

    @JsonProperty("BuyerEmail")
    private String buyerEmail;

    @JsonProperty("BuyerMobile")
    private String buyerMobile;

    @JsonProperty("BuyerPlateNo")
    private String buyerPlateNo;

    @JsonProperty("ReceiveTypeID")
    private Integer receiveTypeId;

    @JsonProperty("ReceiverEmail")
    private String receiverEmail;

    @JsonProperty("ExtraMetadata")
    private String extraMetadata; // JSON string

    @JsonProperty("DeliveryInfo")
    private String deliveryInfo; // JSON string

    @JsonProperty("TaxSummaryJson")
    private String taxSummaryJson; // JSON string

    @JsonProperty("CurrencyCode")
    private String currencyCode;

    @JsonProperty("ExchangeRate")
    private BigDecimal exchangeRate;

    @JsonProperty("GrossAmount")
    private BigDecimal grossAmount;

    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;

    @JsonProperty("NetAmount")
    private BigDecimal netAmount;

    @JsonProperty("TaxAmount")
    private BigDecimal taxAmount;

    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("TotalAmountText")
    private String totalAmountText;

    @JsonProperty("Notes")
    private String notes;

    @JsonProperty("OrgInvoiceID")
    private Long orgInvoiceId;

    @JsonProperty("OrgInvoiceForm")
    private String orgInvoiceForm;

    @JsonProperty("OrgInvoiceSeries")
    private String orgInvoiceSeries;

    @JsonProperty("OrgInvoiceNo")
    private String orgInvoiceNo;

    @JsonProperty("OrgInvoiceDate")
    private LocalDateTime orgInvoiceDate;

    @JsonProperty("OrgInvoiceReason")
    private String orgInvoiceReason;

    @JsonProperty("Details")
    private List<EinvInvoiceDetailDto> details;
}