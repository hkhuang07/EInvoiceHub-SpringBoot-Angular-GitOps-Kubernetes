package com.einvoicehub.core.dto.request.SubmitInvoice;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitInvoiceDetailRequest {

    @NotNull(message = "Số thứ tự dòng (LineNo) không được để trống")
    @JsonProperty("LineNo")
    private Integer lineNo;

    @JsonProperty("ItemCode")
    private String itemCode;

    @NotBlank(message = "Tên hàng hóa/dịch vụ không được để trống")
    @JsonProperty("ItemName")
    private String itemName;

    @JsonProperty("ItemTypeID")
    private Integer itemTypeId; // 1: Hàng hóa, 2: Khuyến mại, 3: Chiết khấu...

    @JsonProperty("UnitName")
    private String UnitName;

    @NotNull(message = "Số lượng không được để trống")
    @DecimalMin(value = "0.000000", message = "Số lượng không được âm")
    @JsonProperty("Quantity")
    private BigDecimal quantity;

    @NotNull(message = "Đơn giá không được để trống")
    @DecimalMin(value = "0.00", message = "Đơn giá không được âm")
    @JsonProperty("Price")
    private BigDecimal price;

    @DecimalMin("0.00")
    @JsonProperty("GrossAmount")
    private BigDecimal grossAmount;

    @JsonProperty("DiscountRate")
    private BigDecimal discountRate;

    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;

    @JsonProperty("TaxTypeID")
    private String taxTypeId;

    @JsonProperty("TaxRate")
    private BigDecimal taxRate;

    @JsonProperty("TaxAmount")
    private BigDecimal taxAmount;

    @NotNull(message = "Tổng tiền dòng hàng không được để trống")
    @DecimalMin("0.00")
    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("IsFree")
    private Boolean isFree;

    @JsonProperty("Notes")
    private String notes;
}