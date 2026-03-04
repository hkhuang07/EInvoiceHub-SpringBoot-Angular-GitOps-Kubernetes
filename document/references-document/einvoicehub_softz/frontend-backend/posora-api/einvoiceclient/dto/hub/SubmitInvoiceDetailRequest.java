package vn.softz.app.einvoiceclient.dto.hub;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitInvoiceDetailRequest {

    @JsonProperty("ItemTypeID")
    private Integer itemTypeId;

    @JsonProperty("ItemCode")
    private String itemCode;

    @JsonProperty("ItemName")
    private String itemName;

    @JsonProperty("UnitName")
    private String unitName;

    @JsonProperty("Quantity")
    private BigDecimal quantity;

    @JsonProperty("Price")
    private BigDecimal price;

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
}
