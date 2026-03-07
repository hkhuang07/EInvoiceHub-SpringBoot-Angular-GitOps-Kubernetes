package com.einvoicehub.server.dto.catalog;

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
public class EinvProviderDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("provider_code")
    @NotBlank(message = "Provider code is required")
    @Size(max = 36, message = "Provider code must not exceed 36 characters")
    private String providerCode;

    @JsonProperty("provider_name")
    @Size(max = 200, message = "Provider name must not exceed 200 characters")
    private String providerName;

    @JsonProperty("integration_url")
    @Size(max = 200, message = "Integration URL must not exceed 200 characters")
    private String integrationUrl;

    @JsonProperty("lookup_url")
    @Size(max = 200, message = "Lookup URL must not exceed 200 characters")
    private String lookupUrl;

    @JsonProperty("inactive")
    @Default
    private Boolean inactive = false;

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