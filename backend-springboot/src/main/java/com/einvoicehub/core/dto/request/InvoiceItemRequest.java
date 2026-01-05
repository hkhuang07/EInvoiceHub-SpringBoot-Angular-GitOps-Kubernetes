package com.einvoicehub.core.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemRequest {

    @NotBlank(message = "VALIDATION_ERROR")
    @Size(max = 500, message = "VALIDATION_ERROR")
    private String itemName;

    @Size(max = 100, message = "VALIDATION_ERROR")
    private String itemCode;

    @NotBlank(message = "VALIDATION_ERROR")
    @Size(max = 50, message = "VALIDATION_ERROR")
    private String unitName;

    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.000001", message = "VALIDATION_ERROR")
    private BigDecimal quantity;

    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", message = "VALIDATION_ERROR")
    private BigDecimal unitPrice;

    private BigDecimal amount;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", message = "VALIDATION_ERROR")
    private BigDecimal taxRate;

    private BigDecimal taxAmount;
    private BigDecimal totalAmountWithVat;
    private String description;
    private String taxCategoryCode;

    /* Normalize financial data to ensure 100% precision before provider submission */
    public void normalizeData() {
        if (quantity != null && unitPrice != null) {
            this.amount = quantity.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);
        }

        if (amount != null && taxRate != null) {
            BigDecimal baseAmount = amount.subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
            this.taxAmount = baseAmount.multiply(taxRate)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        if (amount != null && taxAmount != null) {
            BigDecimal netAmount = amount.subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
            this.totalAmountWithVat = netAmount.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
        }
    }
}