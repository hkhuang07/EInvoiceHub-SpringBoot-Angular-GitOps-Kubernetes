package vn.softz.app.einvoicehub.provider.bkav.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BkavResponse {
    
    private static final ObjectMapper MAPPER = createMapper();
    
    @JsonProperty("Status")
    private Integer status;
    
    @JsonProperty("Object")
    private Object object;
    
    @JsonProperty("Permission")
    private Object permission;
    
    @JsonProperty("Code")
    private Integer code;
    
    @JsonProperty("Message")
    private String message;
    
    @JsonProperty("isOk")
    private Boolean isOk;
    
    @JsonProperty("isError")
    private Boolean isError;
    
    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
    
    public boolean isSuccess() {
        return (isOk != null && isOk) || (status != null && status == 0);
    }
    
    public String getErrorMessage() {
        if (message != null) return message;
        if (object != null && !isSuccess()) return object.toString();
        return "Unknown error";
    }
    
    public Integer getCode() {
        return code != null ? code : status;
    }
    
    public <T> T parseObject(Class<T> clazz) {
        if (object == null) return null;
        try {
            if (object instanceof String) {
                return MAPPER.readValue((String) object, clazz);
            }
            return MAPPER.convertValue(object, clazz);
        } catch (Exception e) {
            log.error("Failed to parse BkavResponse.object to {}: {}", clazz.getSimpleName(), e.getMessage());
            return null;
        }
    }
    
    public <T> List<T> parseObjectList(Class<T> elementType) {
        if (object == null) return null;
        try {
            if (object instanceof String) {
                return MAPPER.readValue((String) object, 
                    MAPPER.getTypeFactory().constructCollectionType(List.class, elementType));
            }
            return MAPPER.convertValue(object, 
                MAPPER.getTypeFactory().constructCollectionType(List.class, elementType));
        } catch (Exception e) {
            log.error("Failed to parse BkavResponse.object to List<{}>: {}", elementType.getSimpleName(), e.getMessage());
            return null;
        }
    }
    
    public List<InvoiceResult> asInvoiceResults() {
        return parseObjectList(InvoiceResult.class);
    }
    
    public InvoiceResult asInvoiceResult() {
        List<InvoiceResult> results = asInvoiceResults();
        return (results != null && !results.isEmpty()) ? results.get(0) : null;
    }
    
    public InvoiceFullData asInvoiceFullData() {
        return parseObject(InvoiceFullData.class);
    }
    
    public Integer asStatusId() {
        if (object == null) return null;
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }
        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public InvoiceFile asInvoiceFile() {
        return parseObject(InvoiceFile.class);
    }
    
    public List<InvoiceStatus> asInvoiceStatusList() {
        return parseObjectList(InvoiceStatus.class);
    }
    
    public List<InvoiceFullData> asInvoiceList() {
        return parseObjectList(InvoiceFullData.class);
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InvoiceResult {
        
        @JsonProperty("PartnerInvoiceID")
        private Long partnerInvoiceID;
        
        @JsonProperty("PartnerInvoiceStringID")
        private String partnerInvoiceStringID;
        
        @JsonProperty("InvoiceGUID")
        private UUID invoiceGUID;
        
        @JsonProperty("InvoiceForm")
        private String invoiceForm;
        
        @JsonProperty("InvoiceSerial")
        private String invoiceSerial;
        
        @JsonProperty("InvoiceNo")
        private Integer invoiceNo;
        
        @JsonProperty("MTC")
        private String mtc;
        
        @JsonProperty("MaCuaCQT")
        private String maCuaCQT;
        
        @JsonProperty("Status")
        private Integer status;
        
        @JsonProperty("MessLog")
        private String messLog;
        
        public boolean isSuccess() {
            return status == null || status == 0;
        }
        
        public String getInvoiceLookupCode() {
            return mtc;
        }
        
        public String getTaxAuthorityCode() {
            return maCuaCQT;
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InvoiceFullData {
        
        @JsonProperty("Invoice")
        private InvoiceHeader invoice;
        
        @JsonProperty("ListInvoiceDetailsWS")
        private List<Detail> details;
        
        @JsonProperty("ListInvoiceAttachFileWS")
        private List<Attachment> attachments;
        
        @JsonProperty("PartnerInvoiceID")
        private Long partnerInvoiceID;
        
        @JsonProperty("PartnerInvoiceStringID")
        private String partnerInvoiceStringID;
        
        public Instant getInvoiceDate() {
            return invoice != null ? invoice.getInvoiceDateAsInstant() : null;
        }
        
        public Instant getSignedDate() {
            return invoice != null ? invoice.getSignedDateAsInstant() : null;
        }
        
        public String getInvoiceCode() {
            return invoice != null ? invoice.getInvoiceCode() : null;
        }
        
        public Integer getInvoiceStatusId() {
            return invoice != null ? invoice.getInvoiceStatusID() : null;
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InvoiceHeader {
        
        private static final DateTimeFormatter BKAV_DATE_FORMAT = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS]");
        
        @JsonProperty("InvoiceTypeID")
        private Integer invoiceTypeID;
        
        @JsonProperty("InvoiceDate")
        private String invoiceDate;
        
        @JsonProperty("SignedDate")
        private String signedDate;
        
        @JsonProperty("InvoiceGUID")
        private UUID invoiceGUID;
        
        @JsonProperty("InvoiceForm")
        private String invoiceForm;
        
        @JsonProperty("InvoiceSerial")
        private String invoiceSerial;
        
        @JsonProperty("InvoiceNo")
        private Integer invoiceNo;
        
        @JsonProperty("InvoiceCode")
        private String invoiceCode;
        
        @JsonProperty("InvoiceStatusID")
        private Integer invoiceStatusID;
        
        @JsonProperty("MaCuaCQT")
        private String maCuaCQT;
        
        @JsonProperty("BuyerName")
        private String buyerName;
        
        @JsonProperty("BuyerTaxCode")
        private String buyerTaxCode;
        
        @JsonProperty("BuyerUnitName")
        private String buyerUnitName;
        
        @JsonProperty("BuyerAddress")
        private String buyerAddress;
        
        @JsonProperty("SumItemAmount")
        private Double sumItemAmount;

        @JsonProperty("PaymentMethodID")
        private Integer paymentMethodId;
        
        @JsonProperty("SumDiscountAmount")
        private Double sumDiscountAmount;
        
        @JsonProperty("SumTaxAmount")
        private Double sumTaxAmount;
        
        @JsonProperty("SumPaymentAmount")
        private Double sumPaymentAmount;
        
        @JsonProperty("SellerTaxCode")
        private String sellerTaxCode;
        
        @JsonProperty("Note")
        private String note;
        
        @JsonProperty("CurrencyID")
        private String currencyID;
        
        @JsonProperty("ExchangeRate")
        private Double exchangeRate;
        
        public Instant getInvoiceDateAsInstant() {
            return parseDate(invoiceDate);
        }
        
        public Instant getSignedDateAsInstant() {
            return parseDate(signedDate);
        }
        
        private Instant parseDate(String dateStr) {
            if (dateStr == null || dateStr.isEmpty()) return null;
            try {
                LocalDateTime ldt = LocalDateTime.parse(dateStr, BKAV_DATE_FORMAT);
                return ldt.toInstant(ZoneOffset.of("+07:00"));
            } catch (Exception e) {
                log.warn("Cannot parse date: {}", dateStr);
                return null;
            }
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InvoiceFile {
        
        @JsonProperty("PDF")
        private String pdf;
        
        @JsonProperty("XML")
        private String xml;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InvoiceStatus {
        
        @JsonProperty("InvoiceGUID")
        private UUID invoiceGUID;
        
        @JsonProperty("InvoiceStatusID")
        private Integer invoiceStatusID;
        
        @JsonProperty("MaCuaCQT")
        private String maCuaCQT;
        
        @JsonProperty("InvoiceCode")
        private String invoiceCode;
        
        @JsonProperty("MTDiep")
        private String mtDiep;
        
        @JsonProperty("ErrorCodes")
        private String errorCodes;
        
        @JsonProperty("ErrorMessages")
        private String errorMessages;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {
        
        @JsonProperty("ItemTypeID")
        private Integer itemTypeID;
        
        @JsonProperty("ItemCode")
        private String itemCode;
        
        @JsonProperty("ItemName")
        private String itemName;
        
        @JsonProperty("UnitName")
        private String unitName;
        
        @JsonProperty("Qty")
        private Double qty;
        
        @JsonProperty("Price")
        private Double price;
        
        @JsonProperty("Amount")
        private Double amount;
        
        @JsonProperty("TaxRateID")
        private Integer taxRateID;
        
        @JsonProperty("TaxRate")
        private Double taxRate;
        
        @JsonProperty("TaxAmount")
        private Double taxAmount;
        
        @JsonProperty("DiscountRate")
        private Double discountRate;
        
        @JsonProperty("DiscountAmount")
        private Double discountAmount;
        
        @JsonProperty("SVChargeAmount")
        private Double svChargeAmount;
        
        @JsonProperty("IsDiscount")
        private Boolean isDiscount;
        
        @JsonProperty("IsIncrease")
        private Boolean isIncrease;
        
        @JsonProperty("UserDefineDetails")
        private String userDefineDetails;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attachment {
        
        @JsonProperty("AttachName")
        private String attachName;
        
        @JsonProperty("AttachLink")
        private String attachLink;
        
        @JsonProperty("AttachData")
        private String attachData;
        
        @JsonProperty("AttachDescription")
        private String attachDescription;
    }
}
