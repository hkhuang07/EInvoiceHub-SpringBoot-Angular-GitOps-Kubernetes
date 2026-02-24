package com.einvoicehub.core.provider.model;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailData {
    private Integer lineNo;
    private Integer itemTypeId;
    private String itemName;
    private String itemCode;
    private String unitName;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private Integer taxRateId;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private Boolean isDiscount;
    private Boolean isIncrease;
    private String userDefineDetails;
    private String notes;
}