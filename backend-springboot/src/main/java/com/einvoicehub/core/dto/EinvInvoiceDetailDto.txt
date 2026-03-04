package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceDetailDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("DocID")
    private Long invoiceId;

    @JsonProperty("LineNo")
    private Integer lineNo;

    @JsonProperty("ItemID")
    private String itemId;

    @JsonProperty("ItemCode")
    private String itemCode;

    @JsonProperty("ItemName")
    private String itemName;

    @JsonProperty("ItemTypeID")
    private Integer itemTypeId;

    @JsonProperty("IsFree")
    private Boolean isFree;

    @JsonProperty("UnitName")
    private String unitName;

    @JsonProperty("Quantity")
    private BigDecimal quantity;

    @JsonProperty("Price")
    private BigDecimal Price;

    @JsonProperty("GrossAmount")
    private BigDecimal grossAmount;

    @JsonProperty("DiscountRate")
    private BigDecimal discountRate;

    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;

    @JsonProperty("NetAmount")
    private BigDecimal netAmount;

    @JsonProperty("NetPrice")
    private BigDecimal netPrice;

    @JsonProperty("TaxTypeID")
    private String taxTypeId;

    @JsonProperty("TaxRate")
    private BigDecimal taxRate;

    @JsonProperty("TaxAmount")
    private BigDecimal taxAmount;

    @JsonProperty("NetPriceVat")
    private BigDecimal netPriceVat;

    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("AdjustmentType")
    private Integer adjustmentType;

    @JsonProperty("Notes")
    private String notes;
}