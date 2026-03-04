package vn.softz.app.einvoicehub.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetInvoicesResponse {
    
    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;
    
    @JsonProperty("InvoiceID")
    private String invoiceId;
    
    @JsonProperty("InvoiceTypeID")
    private Integer invoiceTypeId;
    
    @JsonProperty("ReferenceTypeID")
    private String referenceTypeId;
    
    @JsonProperty("InvoiceDate")
    private String invoiceDate;
    
    @JsonProperty("InvoiceForm")
    private String invoiceForm;
    
    @JsonProperty("InvoiceNo")
    private String invoiceNo;
    
    @JsonProperty("PaymentMethodID")
    private Integer paymentMethodId;
    
    @JsonProperty("BuyerTaxCode")
    private String buyerTaxCode;
    
    @JsonProperty("BuyerCompany")
    private String buyerCompany;
    
    @JsonProperty("BuyerName")
    private String buyerName;
    
    @JsonProperty("BuyerAddress")
    private String buyerAddress;
    
    @JsonProperty("BuyerIDNo")
    private String buyerIdNo;
    
    @JsonProperty("BuyerMobile")
    private String buyerMobile;
    
    @JsonProperty("BuyerBankAccount")
    private String buyerBankAccount;
    
    @JsonProperty("BuyerBankName")
    private String buyerBankName;
    
    @JsonProperty("BuyerBudgetCode")
    private String buyerBudgetCode;
    
    @JsonProperty("ReceiveTypeID")
    private Integer receiveTypeId;
    
    @JsonProperty("ReceiverEmail")
    private String receiverEmail;
    
    @JsonProperty("CurrencyCode")
    private String currencyCode;
    
    @JsonProperty("ExchangeRate")
    private BigDecimal exchangeRate;
    
    @JsonProperty("InvoiceLookupCode")
    private String invoiceLookupCode;
    
    @JsonProperty("TaxAuthorityCode")
    private String taxAuthorityCode;
    
    @JsonProperty("Provider")
    private String provider;
    
    @JsonProperty("ProviderInvoiceID")
    private String providerInvoiceId;
    
    @JsonProperty("UrlLookup")
    private String urlLookup;
    
    @JsonProperty("GrossAmount")
    private BigDecimal grossAmount;
    
    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;
    
    @JsonProperty("TaxAmount")
    private BigDecimal taxAmount;
    
    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;
    
    @JsonProperty("SignedDate")
    private String signedDate;
    
    @JsonProperty("InvoiceStatusID")
    private Integer invoiceStatusId;
    
    @JsonProperty("Notes")
    private String notes;
    
    @JsonProperty("Details")
    private List<GetInvoiceDetailResponse> details;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetInvoiceDetailResponse {
        
        @JsonProperty("ItemTypeID")
        private Integer itemTypeId;
        
        @JsonProperty("ItemCode")
        private String itemCode;
        
        @JsonProperty("ItemName")
        private String itemName;
        
        @JsonProperty("UnitName")
        private String unitName;
        
        @JsonProperty("Quantity")
        private BigDecimal quantity;
        
        @JsonProperty("Price")
        private BigDecimal price;
        
        @JsonProperty("GrossAmount")
        private BigDecimal grossAmount;
        
        @JsonProperty("DiscountRate")
        private BigDecimal discountRate;
        
        @JsonProperty("DiscountAmount")
        private BigDecimal discountAmount;
        
        @JsonProperty("TaxTypeID")
        private String taxTypeId;
        
        @JsonProperty("TaxRate")
        private BigDecimal taxRate;
        
        @JsonProperty("TaxAmount")
        private BigDecimal taxAmount;
        
        @JsonProperty("TotalAmount")
        private BigDecimal totalAmount;
    }
}