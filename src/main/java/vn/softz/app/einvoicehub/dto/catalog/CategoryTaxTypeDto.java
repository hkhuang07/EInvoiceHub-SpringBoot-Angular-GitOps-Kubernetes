package vn.softz.app.einvoicehub.dto.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryTaxTypeDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tax_name")
    @Size(max = 100, message = "Tax name must not exceed 100 characters")
    private String taxName;

    @JsonProperty("tax_name_en")
    @Size(max = 100, message = "Tax name (EN) must not exceed 100 characters")
    private String taxNameEn;

    @JsonProperty("description")
    @Size(max = 100, message = "Description must not exceed 100 characters")
    private String description;

    @JsonProperty("vat")
    @DecimalMin(value = "0.0", inclusive = true, message = "VAT rate must be >= 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "VAT rate must be <= 100")
    private BigDecimal vat;

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