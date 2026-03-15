package vn.softz.app.einvoicehub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.Builder.Default;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvInvoiceDetailDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenant_id")
    @Size(max = 36, message = "Tenant ID must not exceed 36 characters")
    private String tenantId;

    @JsonProperty("store_id")
    @Size(max = 36, message = "Store ID must not exceed 36 characters")
    private String storeId;

    @JsonProperty("doc_id")
    @Size(max = 36, message = "Document ID must not exceed 36 characters")
    private String docId;

    @JsonProperty("line_no")
    @Min(value = 1, message = "Line number must be > 0")
    private Integer lineNo;

    @JsonProperty("is_free")
    @Default
    private Boolean isFree = false;

    @JsonProperty("item_type_id")
    @Min(value = 0, message = "Item type ID must be >= 0")
    @Max(value = 17, message = "Item type ID must be <= 17")
    private Byte itemTypeId;

    @JsonProperty("item_id")
    @Size(max = 36, message = "Item ID must not exceed 36 characters")
    private String itemId;

    @JsonProperty("item_name")
    @NotBlank(message = "Item name is required")
    @Size(max = 500, message = "Item name must not exceed 500 characters")
    private String itemName;

    @JsonProperty("unit")
    @Size(max = 50, message = "Unit must not exceed 50 characters")
    private String unit;

    @JsonProperty("quantity")
    @DecimalMin(value = "0.01", inclusive = true, message = "Quantity must be > 0")
    private BigDecimal quantity;

    @JsonProperty("price")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be >= 0")
    private BigDecimal price;

    @JsonProperty("gross_amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Gross amount must be >= 0")
    private BigDecimal grossAmount;

    @JsonProperty("discount_rate")
    @DecimalMin(value = "0.0", inclusive = true, message = "Discount rate must be >= 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Discount rate must be <= 100")
    private BigDecimal discountRate;

    @JsonProperty("discount_amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Discount amount must be >= 0")
    private BigDecimal discountAmount;

    @JsonProperty("net_price_vat")
    @DecimalMin(value = "0.0", inclusive = true, message = "Net price VAT must be >= 0")
    private BigDecimal netPriceVat;

    @JsonProperty("net_price")
    @DecimalMin(value = "0.0", inclusive = true, message = "Net price must be >= 0")
    private BigDecimal netPrice;

    @JsonProperty("net_amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Net amount must be >= 0")
    private BigDecimal netAmount;

    @JsonProperty("tax_type_id")
    @Size(max = 36, message = "Tax type ID must not exceed 36 characters")
    private String taxTypeId;

    @JsonProperty("tax_rate")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tax rate must be >= 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Tax rate must be <= 100")
    private BigDecimal taxRate;

    @JsonProperty("tax_amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tax amount must be >= 0")
    private BigDecimal taxAmount;

    @JsonProperty("total_amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total amount must be >= 0")
    private BigDecimal totalAmount;

    @JsonProperty("notes")
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    @JsonProperty("adjustment_type")
    @Min(value = 0, message = "Adjustment type must be 0 (default), 1 (info), 2 (increase) or 3 (decrease)")
    @Max(value = 3, message = "Adjustment type must be 0 (default), 1 (info), 2 (increase) or 3 (decrease)")
    @Default
    private Byte adjustmentType = 0;

    @JsonProperty("created_by")
    @Size(max = 36, message = "Created by must not exceed 36 characters")
    private String createdBy;

    @JsonProperty("updated_by")
    @Size(max = 36, message = "Updated by must not exceed 36 characters")
    private String updatedBy;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("updated_date")
    private LocalDateTime updatedDate;
}