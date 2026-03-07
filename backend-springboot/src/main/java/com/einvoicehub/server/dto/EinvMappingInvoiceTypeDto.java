package com.einvoicehub.server.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.Builder.Default;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvMappingInvoiceTypeDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("provider_id")
    @NotBlank(message = "Provider ID is required")
    @Size(max = 36, message = "Provider ID must not exceed 36 characters")
    private String providerId;

    @JsonProperty("invoice_type_id")
    @NotNull(message = "Invoice type ID is required")
    @Min(value = 1, message = "Invoice type ID must be >= 1")
    @Max(value = 11, message = "Invoice type ID must be <= 11")
    private Byte invoiceTypeId;

    @JsonProperty("provider_invoice_type_id")
    @NotBlank(message = "Provider invoice type ID is required")
    @Size(max = 36, message = "Provider invoice type ID must not exceed 36 characters")
    private String providerInvoiceTypeId;

    @JsonProperty("inactive")
    @Min(value = 0, message = "Inactive must be 0 (active) or 1 (disabled)")
    @Max(value = 1, message = "Inactive must be 0 (active) or 1 (disabled)")
    @Default
    private Byte inactive = 0;

    @JsonProperty("note")
    @Size(max = 200, message = "Note must not exceed 200 characters")
    private String note;

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

    // --- Các trường join/display (không lưu DB) ---

    @JsonProperty("provider_name")
    private String providerName;

    @JsonProperty("invoice_type_name")
    private String invoiceTypeName;
}