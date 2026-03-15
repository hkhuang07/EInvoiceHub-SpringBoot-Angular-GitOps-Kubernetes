package vn.softz.app.einvoicehub.dto;

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
public class EinvMappingInvoiceStatusDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("provider_id")
    @NotBlank(message = "Provider ID is required")
    @Size(max = 36, message = "Provider ID must not exceed 36 characters")
    private String providerId;

    @JsonProperty("invoice_status_id")
    @NotNull(message = "Invoice status ID is required")
    private Boolean invoiceStatusId;

    @JsonProperty("provider_invoice_status_id")
    @NotBlank(message = "Provider status ID is required")
    @Size(max = 36, message = "Provider invoice status ID must not exceed 36 characters")
    private String providerInvoiceStatusId;

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

    @JsonProperty("provider_name")
    private String providerName;

    @JsonProperty("invoice_status_name")
    private String invoiceStatusName;
}