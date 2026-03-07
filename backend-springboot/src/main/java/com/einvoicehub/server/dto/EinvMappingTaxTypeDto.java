package com.einvoicehub.server.dto;

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
public class EinvMappingTaxTypeDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("provider_id")
    @NotBlank(message = "Provider ID is required")
    @Size(max = 36, message = "Provider ID must not exceed 36 characters")
    private String providerId;

    @JsonProperty("tax_type_id")
    @Size(max = 36, message = "Tax type ID must not exceed 36 characters")
    private String taxTypeId;

    @JsonProperty("provider_tax_type_id")
    @NotBlank(message = "Provider tax type ID is required")
    @Size(max = 36, message = "Provider tax type ID must not exceed 36 characters")
    private String providerTaxTypeId;

    @JsonProperty("provider_tax_rate")
    @Size(max = 36, message = "Provider tax rate must not exceed 36 characters")
    private String providerTaxRate;

    @JsonProperty("vat_rate")
    @DecimalMin(value = "0.0", inclusive = true, message = "VAT rate must be >= 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "VAT rate must be <= 100")
    private BigDecimal vatRate;

    @JsonProperty("inactive")
    @Min(value = 0, message = "Inactive must be 0 (active) or 1 (disabled)")
    @Max(value = 1, message = "Inactive must be 0 (active) or 1 (disabled)")
    @Default
    private Byte inactive = 0;

    @JsonProperty("note")
    @Size(max = 200, message = "Note must not exceed 200 characters")
    private String note;

    @JsonProperty("created_by")
    @Size(max = 100, message = "Created by must not exceed 100 characters")
    private String createdBy;

    @JsonProperty("updated_by")
    @Size(max = 100, message = "Updated by must not exceed 100 characters")
    private String updatedBy;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("updated_date")
    private LocalDateTime updatedDate;

    // Các trường join/display (không lưu DB)

    /** Tên nhà cung cấp (display) */
    @JsonProperty("provider_name")
    private String providerName;

    /** Tên loại thuế HUB (display) */
    @JsonProperty("tax_type_name")
    private String taxTypeName;
}