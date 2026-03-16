package vn.softz.app.einvoicehub.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Dòng hàng hóa trên hóa đơn — dùng cho cả SubmitInvoice và SubmitAdjustInvoice.
 * Logic tính toán Service:
 *   net_amount     = gross_amount - discount_amount
 *   total_amount   = net_amount + tax_amount
 *   net_price      = net_amount / quantity
 *   net_price_vat  = total_amount / quantity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmitInvoiceDetailRequest {

    @Min(value = 1, message = "Line number must be >= 1")
    @JsonProperty("line_no")
    private Integer lineNo;

    @NotNull(message = "Item type ID is required")
    @Min(value = 0, message = "Item type ID must be >= 0")
    @Max(value = 17, message = "Item type ID must be <= 17")
    @JsonProperty("item_type_id")
    private Byte itemTypeId;

    @Size(max = 36, message = "Item ID must not exceed 36 characters")
    @JsonProperty("item_id")
    private String itemId;

    @NotBlank(message = "Product name is required")
    @Size(max = 500, message = "Product name must not exceed 500 characters")
    @JsonProperty("item_name")
    private String itemName;

    @NotBlank(message = "Unit is required")
    @Size(max = 50, message = "Unit must not exceed 50 characters")
    @JsonProperty("unit")
    private String unit;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be > 0")
    @JsonProperty("quantity")
    private BigDecimal quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", message = "Unit price must be >= 0")
    @JsonProperty("price")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "Gross amount must be >= 0")
    @JsonProperty("gross_amount")
    private BigDecimal grossAmount;

    @DecimalMin(value = "0.0", message = "Discount rate must be >= 0")
    @DecimalMax(value = "100.0", message = "Discount rate must not exceed 100%")
    @JsonProperty("discount_rate")
    private BigDecimal discountRate;

    @DecimalMin(value = "0.0", message = "Discount amount must be >= 0")
    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    /** Thành tiền sau chiết khấu = gross_amount - discount_amount | DECIMAL(15,2) */
    @DecimalMin(value = "0.0", message = "Net amount must be >= 0")
    @JsonProperty("net_amount")
    private BigDecimal netAmount;

    /** Đơn giá sau chiết khấu = net_amount / quantity | DECIMAL(15,2) */
    @DecimalMin(value = "0.0", message = "Net price must be >= 0")
    @JsonProperty("net_price")
    private BigDecimal netPrice;

    /** Đơn giá có VAT = total_amount / quantity | DECIMAL(15,2) */
    @DecimalMin(value = "0.0", message = "Net price with VAT must be >= 0")
    @JsonProperty("net_price_vat")
    private BigDecimal netPriceVat;

    @Size(max = 36, message = "Tax type ID must not exceed 36 characters")
    @JsonProperty("tax_type_id")
    private String taxTypeId;

    /**Giá trị hợp lệ theo schema: 0, 3.5, 5, 7, 8, 10*/
    @DecimalMin(value = "0.0", message = "Tax rate must be >= 0")
    @DecimalMax(value = "100.0", message = "Tax rate must not exceed 100%")
    @JsonProperty("tax_rate")
    private BigDecimal taxRate;

    @DecimalMin(value = "0.0", message = "Tax amount must be >= 0")
    @JsonProperty("tax_amount")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0.0", message = "Total amount must be >= 0")
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    /**   0 = Không điều chỉnh (HĐ Gốc)
     *   1 = Điều chỉnh thông tin
     *   2 = Tăng
     *   3 = Giảm
     * Default: 0 */
    @Min(value = 0, message = "Adjustment type must be 0, 1, 2, or 3")
    @Max(value = 3, message = "Adjustment type must be 0, 1, 2, or 3")
    @JsonProperty("adjustment_type")
    @Builder.Default
    private Byte adjustmentType = 0;

    @Builder.Default
    @JsonProperty("is_free")
    private Boolean isFree = false;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @JsonProperty("notes")
    private String notes;
}