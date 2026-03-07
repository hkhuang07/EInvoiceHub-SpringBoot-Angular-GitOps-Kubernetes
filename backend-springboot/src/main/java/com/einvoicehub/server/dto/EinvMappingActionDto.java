package com.einvoicehub.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvMappingActionDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("provider_id")
    @NotBlank(message = "Provider ID is required")
    @Size(max = 36, message = "Provider ID must not exceed 36 characters")
    private String providerId;

    @JsonProperty("hub_action")
    @NotBlank(message = "Hub action is required")
    @Size(max = 50, message = "Hub action must not exceed 50 characters")
    private String hubAction;

    @JsonProperty("provider_cmd")
    @NotBlank(message = "Provider command is required")
    @Size(max = 100, message = "Provider command must not exceed 100 characters")
    private String providerCmd;

    @JsonProperty("description")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

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
}