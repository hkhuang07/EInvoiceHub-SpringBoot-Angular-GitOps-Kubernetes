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
public class EinvMappingItemTypeDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("provider_id")
    @NotBlank(message = "Provider ID is required")
    @Size(max = 36, message = "Provider ID must not exceed 36 characters")
    private String providerId;

    @JsonProperty("item_type_id")
    @NotNull(message = "Item type ID is required")
    @Min(value = 0, message = "Item type ID must be >= 0")
    @Max(value = 17, message = "Item type ID must be <= 17")
    private Byte itemTypeId;

    @JsonProperty("provider_item_type_id")
    @NotBlank(message = "Provider item type ID is required")
    @Size(max = 36, message = "Provider item type ID must not exceed 36 characters")
    private String providerItemTypeId;

    @JsonProperty("inactive")
    @Default
    private Boolean inactive = false;

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

    // --- Các trường join/display (không lưu DB) ---

    @JsonProperty("provider_name")
    private String providerName;

    @JsonProperty("item_type_name")
    private String itemTypeName;
}