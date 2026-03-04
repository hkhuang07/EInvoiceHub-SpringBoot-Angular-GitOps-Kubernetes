package vn.softz.app.einvoicehub.provider.bkav.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class BkavInvoice {
    
    private BkavInvoice() {}
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequestData {
        
        @JsonProperty("Invoice")
        private Header invoice;
        
        @JsonProperty("ListInvoiceDetailsWS")
        private List<Detail> listInvoiceDetailsWS;
        
        @JsonProperty("ListInvoiceAttachFileWS")
        @Builder.Default
        private List<Attachment> listInvoiceAttachFileWS = List.of();
        
        @JsonProperty("AttachListNumber")
        @Builder.Default
        private String attachListNumber = "";
        
        @JsonProperty("AttachListDate")
        @Builder.Default
        private String attachListDate = "";
        
        @JsonProperty("PartnerInvoiceID")
        private Long partnerInvoiceID;
        
        @JsonProperty("PartnerInvoiceStringID")
        @Builder.Default
        private String partnerInvoiceStringID = "";
    }
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        
        @JsonProperty("InvoiceTypeID")
        @Builder.Default
        private Integer invoiceTypeID = 1;
        
        @JsonProperty("InvoiceDate")
        private String invoiceDate;
        
        @JsonProperty("BuyerCode")
        @Builder.Default
        private String buyerCode = "";
        
        @JsonProperty("BuyerName")
        private String buyerName;
        
        @JsonProperty("BuyerTaxCode")
        @Builder.Default
        private String buyerTaxCode = "";
        
        @JsonProperty("BuyerUnitName")
        @Builder.Default
        private String buyerUnitName = "";
        
        @JsonProperty("BuyerAddress")
        @Builder.Default
        private String buyerAddress = "";
        
        @JsonProperty("BuyerBankAccount")
        @Builder.Default
        private String buyerBankAccount = "";
        
        @JsonProperty("PayMethodID")
        @Builder.Default
        private Integer payMethodID = 3;
        
        @JsonProperty("ReceiveTypeID")
        @Builder.Default
        private Integer receiveTypeID = 3;
        
        @JsonProperty("ReceiverEmail")
        @Builder.Default
        private String receiverEmail = "";
        
        @JsonProperty("ReceiverMobile")
        @Builder.Default
        private String receiverMobile = "";
        
        @JsonProperty("ReceiverAddress")
        @Builder.Default
        private String receiverAddress = "";
        
        @JsonProperty("ReceiverName")
        @Builder.Default
        private String receiverName = "";
        
        @JsonProperty("Note")
        @Builder.Default
        private String note = "";
        
        @JsonProperty("BillCode")
        @Builder.Default
        private String billCode = "";
        
        @JsonProperty("CurrencyID")
        @Builder.Default
        private String currencyID = "VND";
        
        @JsonProperty("ExchangeRate")
        @Builder.Default
        private Double exchangeRate = 1.0;
        
        @JsonProperty("InvoiceForm")
        @Builder.Default
        private String invoiceForm = "1";
        
        @JsonProperty("InvoiceSerial")
        private String invoiceSerial;
        
        @JsonProperty("InvoiceNo")
        private Integer invoiceNo;
        
        @JsonProperty("InvoiceGUID")
        private UUID invoiceGUID;
        
        @JsonProperty("MaCuaCQT")
        @Builder.Default
        private String maCuaCQT = "";
        
        @JsonProperty("CCCD")
        @Builder.Default
        private String cccd = "";
        
        @JsonProperty("UserDefine")
        @Builder.Default
        private String userDefine = "";
    }
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {
        
        @JsonProperty("ItemTypeID")
        @Builder.Default
        private Integer itemTypeID = 0;
        
        @JsonProperty("ItemCode")
        @Builder.Default
        private String itemCode = "";
        
        @JsonProperty("ItemName")
        private String itemName;
        
        @JsonProperty("UnitName")
        private String unitName;
        
        @JsonProperty("Qty")
        private BigDecimal qty;
        
        @JsonProperty("Price")
        private BigDecimal price;
        
        @JsonProperty("Amount")
        private BigDecimal amount;
        
        @JsonProperty("TaxRateID")
        @Builder.Default
        private Integer taxRateID = 3;
        
        @JsonProperty("TaxRate")
        private BigDecimal taxRate;
        
        @JsonProperty("TaxAmount")
        private BigDecimal taxAmount;
        
        @JsonProperty("DiscountRate")
        @Builder.Default
        private BigDecimal discountRate = BigDecimal.ZERO;
        
        @JsonProperty("DiscountAmount")
        @Builder.Default
        private BigDecimal discountAmount = BigDecimal.ZERO;
        
        @JsonProperty("SVChargeAmount")
        @Builder.Default
        private BigDecimal svChargeAmount = BigDecimal.ZERO;
        
        @JsonProperty("IsDiscount")
        @Builder.Default
        private Boolean isDiscount = false;
        
        @JsonProperty("IsIncrease")
        @Builder.Default
        private Boolean isIncrease = false;
        
        @JsonProperty("UserDefineDetails")
        @Builder.Default
        private String userDefineDetails = "";
    }
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attachment {
        
        @JsonProperty("AttachName")
        private String attachName;
        
        @JsonProperty("AttachLink")
        private String attachLink;
        
        @JsonProperty("AttachData")
        private String attachData;
        
        @JsonProperty("AttachDescription")
        @Builder.Default
        private String attachDescription = "";
    }
}
