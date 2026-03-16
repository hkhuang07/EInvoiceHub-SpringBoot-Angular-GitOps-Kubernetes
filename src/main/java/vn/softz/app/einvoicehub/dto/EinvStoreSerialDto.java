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
public class EinvStoreSerialDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenant_id")
    @Size(max = 36, message = "Tenant ID must not exceed 36 characters")
    private String tenantId;

    @JsonProperty("store_id")
    @NotBlank(message = "Store ID is required")
    @Size(max = 36, message = "Store ID must not exceed 36 characters")
    private String storeId;

    @JsonProperty("provider_id")
    @NotBlank(message = "Provider ID is required")
    @Size(max = 36, message = "Provider ID must not exceed 36 characters")
    private String providerId;

    @JsonProperty("invoice_type_id")
    private Byte invoiceTypeId;

    @JsonProperty("provider_serial_id")
    @Size(max = 50, message = "Provider serial ID must not exceed 50 characters")
    private String providerSerialId;

    @JsonProperty("invoice_form")
    @Size(max = 20, message = "Invoice form must not exceed 20 characters")
    private String invoiceForm;

    @JsonProperty("invoice_serial")
    @Size(max = 20, message = "Invoice serial must not exceed 20 characters")
    private String invoiceSerial;

    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @JsonProperty("status")
    @Min(value = 0, message = "Status must be 0 (Chờ duyệt), 1 (Đã duyệt) or 8 (Ngưng sử dụng)")
    @Max(value = 8, message = "Status must be 0 (Chờ duyệt), 1 (Đã duyệt) or 8 (Ngưng sử dụng)")
    @Default
    private Byte status = 1;

    @JsonProperty("created_by")
    private LocalDateTime createdBy;

    @JsonProperty("updated_by")
    private LocalDateTime updatedBy;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("updated_date")
    private LocalDateTime updatedDate;
}