package com.einvoicehub.core.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * DTO cho chi tiết mặt hàng trong hóa đơn.
 * Hợp nhất logic tính toán tự động và hệ thống Validation chuyên nghiệp.
 */
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
    @DecimalMin(value = "0.000001", message = "VALIDATION_ERROR") // Số lượng phải > 0
    private BigDecimal quantity;

    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", message = "VALIDATION_ERROR")
    private BigDecimal unitPrice;

    /** Thành tiền trước thuế và trước chiết khấu (Thường = quantity * unitPrice) */
    private BigDecimal amount;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /** Tỷ lệ thuế suất (0, 5, 8, 10...) */
    @NotNull(message = "VALIDATION_ERROR")
    @DecimalMin(value = "0.0", message = "VALIDATION_ERROR")
    private BigDecimal taxRate;

    /** Tiền thuế GTGT */
    private BigDecimal taxAmount;

    /** Tổng tiền sau chiết khấu và đã bao gồm thuế */
    private BigDecimal totalAmountWithVat;

    private String description;

    /** Mã loại thuế suất (Dùng cho một số Provider đặc thù) */
    private String taxCategoryCode;

    /**
     * CHUẨN HÓA DỮ LIỆU TÀI CHÍNH:
     * Tự động tính toán lại các con số để đảm bảo tính nhất quán trước khi gửi sang Provider.
     * Tránh lỗi lệch 1-2 đồng do Merchant làm tròn sai.
     */
    public void normalizeData() {
        // 1. Tính thành tiền thô: amount = quantity * unitPrice
        if (quantity != null && unitPrice != null) {
            this.amount = quantity.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);
        }

        // 2. Tính tiền thuế: taxAmount = (amount - discountAmount) * taxRate / 100
        if (amount != null && taxRate != null) {
            BigDecimal baseAmount = amount.subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
            this.taxAmount = baseAmount.multiply(taxRate)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        // 3. Tính tổng cộng cuối cùng: totalAmountWithVat = (amount - discountAmount) + taxAmount
        if (amount != null && taxAmount != null) {
            BigDecimal netAmount = amount.subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
            this.totalAmountWithVat = netAmount.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
        }
    }
}