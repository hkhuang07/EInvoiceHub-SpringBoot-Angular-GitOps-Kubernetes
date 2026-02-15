package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceItemDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("LineNumber")
    private Integer lineNumber;

    @JsonProperty("IsFree")
    private Boolean isFree;

    @JsonProperty("ProductCode")
    private String productCode;

    @JsonProperty("ProductName")
    private String productName;

    @JsonProperty("UnitName")
    private String unitName;

    @JsonProperty("Quantity")
    private BigDecimal quantity;

    @JsonProperty("UnitPrice")
    private BigDecimal unitPrice;

    @JsonProperty("GrossAmount")
    private BigDecimal grossAmount;

    @JsonProperty("DiscountRate")
    private BigDecimal discountRate;

    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;

    @JsonProperty("TaxRate")
    private BigDecimal taxRate;

    @JsonProperty("TaxAmount")
    private BigDecimal taxAmount;

    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("Notes")
    private String notes;
}