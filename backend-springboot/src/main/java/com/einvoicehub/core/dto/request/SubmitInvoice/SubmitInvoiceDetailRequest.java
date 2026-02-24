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

    @JsonProperty("ItemCode")
    private String itemCode;

    @NotBlank(message = "Tên hàng hóa không được để trống")
    @JsonProperty("ItemName")
    private String itemName;

    @NotNull(message = "Số lượng là bắt buộc")
    @PositiveOrZero
    @JsonProperty("Quantity")
    private BigDecimal quantity;

    @NotNull(message = "Đơn giá là bắt buộc")
    @PositiveOrZero
    @JsonProperty("Price")
    private BigDecimal price;

    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;

    @NotNull(message = "ID loại thuế là bắt buộc")
    @JsonProperty("TaxTypeID")
    private Long taxTypeId;
}