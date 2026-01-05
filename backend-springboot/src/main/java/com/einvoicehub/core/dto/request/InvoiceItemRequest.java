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


/*
File mới được minimax viết package com.einvoicehub.core.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO cho từng sản phẩm/dịch vụ trong hóa đơn.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemRequest {

    /**
     * Tên sản phẩm/dịch vụ - bắt buộc.
     */
    @NotBlank(message = "VALIDATION_ERROR")
    @Size(max = 500, message = "VALIDATION_ERROR")
    private String itemName;

    /**
     * Mã sản phẩm - tùy chọn.
     */
    @Size(max = 100, message = "VALIDATION_ERROR")
    private String itemCode;

    /**
     * Đơn vị tính - bắt buộc.
     */
    @NotBlank(message = "VALIDATION_ERROR")
    @Size(max = 50, message = "VALIDATION_ERROR")
    private String unit;

    /**
     * Số lượng - bắt buộc, > 0.
     */
    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.01", inclusive = true, message = "VALIDATION_ERROR")
    private BigDecimal quantity;

    /**
     * Đơn giá - bắt buộc, >= 0.
     */
    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", inclusive = true, message = "VALIDATION_ERROR")
    private BigDecimal unitPrice;

    /**
     * Tổng tiền = quantity * unitPrice - bắt buộc, >= 0.
     */
    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", inclusive = true, message = "VALIDATION_ERROR")
    private BigDecimal amount;

    /**
     * Tỷ lệ thuế GTGT (0%, 5%, 10%) - bắt buộc.
     */
    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "10.0", inclusive = true)
    @Pattern(regexp = "^(0|5|10)$|^0\\.0$|^5\\.0$|^10\\.0$",
            message = "VALIDATION_ERROR")
    private BigDecimal vatRate;

    /**
     * Tiền thuế GTGT = amount * vatRate - bắt buộc, >= 0.
     */
    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", inclusive = true, message = "VALIDATION_ERROR")
    private BigDecimal vatAmount;

    /**
     * Tổng tiền bao gồm thuế = amount + vatAmount - bắt buộc, >= 0.
     */
    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", inclusive = true, message = "VALIDATION_ERROR")
    private BigDecimal totalAmountWithVat;

    /**
     * Mã hàng hóa theo quy định - tùy chọn.
     */
    @Size(max = 50, message = "VALIDATION_ERROR")
    private String taxCategoryCode;
}

 */