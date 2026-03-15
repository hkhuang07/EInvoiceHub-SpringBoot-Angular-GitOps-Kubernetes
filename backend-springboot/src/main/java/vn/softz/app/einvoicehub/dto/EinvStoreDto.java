package vn.softz.app.einvoicehub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvStoreDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenant_id")
    @NotBlank(message = "Tenant ID is required")
    @Size(max = 36, message = "Tenant ID must not exceed 36 characters")
    private String tenantId;

    @JsonProperty("store_name")
    @NotBlank(message = "Store name is required")
    @Size(max = 255, message = "Store name must not exceed 255 characters")
    private String storeName;

    @JsonProperty("tax_code")
    @Size(max = 20, message = "Tax code must not exceed 20 characters")
    private String taxCode;

    @JsonProperty("is_active")
    @NotNull(message = "Is active is required")
    @Default
    private Boolean isActive = true;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("updated_date")
    private LocalDateTime updatedDate;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("update_by")
    private String updateBy;

}