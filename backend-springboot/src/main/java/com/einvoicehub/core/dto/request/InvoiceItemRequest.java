package com.einvoicehub.core.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Invoice Item Request DTO - Public API Model
 * Chứa logic chuẩn hóa dữ liệu tài chính của mặt hàng.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemRequest {

    @NotBlank(message = "Item name is required")
    private String itemName;

    private String itemCode;
    private String unitName;
    private String unitCode;

    @DecimalMin(value = "0.0", message = "Quantity must be non-negative")
    @Builder.Default
    private BigDecimal quantity = BigDecimal.ONE;

    @DecimalMin(value = "0.0", message = "Unit price must be non-negative")
    private BigDecimal unitPrice = BigDecimal.ZERO;

    private BigDecimal amount; // Thành tiền chưa thuế, chưa chiết khấu

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;
    private BigDecimal discountPercent;

    @DecimalMin(value = "0.0", message = "Tax rate must be non-negative")
    @Builder.Default
    private BigDecimal taxRate = BigDecimal.ZERO;

    private String taxType;
    private String taxCategory;

    private BigDecimal totalAmount;    // Thành tiền sau chiết khấu, chưa thuế
    private BigDecimal totalTaxAmount; // Tiền thuế

    private String description;
    private String sequence;

    /**
     * Chuẩn hóa toàn bộ dữ liệu tài chính của mặt hàng.
     */
    public void normalizeData() {
        calculateAmount();
        calculateTotalAmount();
        calculateTaxAmount();
    }

    private void calculateAmount() {
        if (quantity != null && unitPrice != null) {
            this.amount = quantity.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);
        }
    }

    private void calculateTotalAmount() {
        if (amount != null) {
            this.totalAmount = amount.subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }

    private void calculateTaxAmount() {
        if (totalAmount != null && taxRate != null) {
            // totalTaxAmount = totalAmount * taxRate / 100
            this.totalTaxAmount = totalAmount.multiply(taxRate)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
    }
}