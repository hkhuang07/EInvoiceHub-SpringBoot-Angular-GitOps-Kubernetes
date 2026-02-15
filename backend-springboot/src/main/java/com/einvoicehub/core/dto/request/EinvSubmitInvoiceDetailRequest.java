package com.einvoicehub.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvSubmitInvoiceDetailRequest {

    @JsonProperty("ItemTypeID")
    private Integer itemTypeId;

    @JsonProperty("ItemCode")
    private String itemCode;

    @NotBlank(message = "Tên hàng hóa không được để trống")
    @JsonProperty("ItemName")
    private String itemName;

    @JsonProperty("UnitName")
    private String unitName;

    @NotNull(message = "Số lượng là bắt buộc")
    @PositiveOrZero
    @JsonProperty("Quantity")
    private BigDecimal quantity;

    @NotNull(message = "Đơn giá là bắt buộc")
    @PositiveOrZero
    @JsonProperty("Price")
    private BigDecimal price;

    @JsonProperty("GrossAmount")
    private BigDecimal grossAmount; //Qty * Price

    @JsonProperty("DiscountRate")
    private BigDecimal discountRate;

    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;

    @NotNull(message = "ID loại thuế là bắt buộc")
    @JsonProperty("TaxTypeID")
    private Long taxTypeId; // Mapping tới vat_rate_id

    @JsonProperty("TaxRate")
    private BigDecimal taxRate;

    @JsonProperty("TaxAmount")
    private BigDecimal taxAmount;
}