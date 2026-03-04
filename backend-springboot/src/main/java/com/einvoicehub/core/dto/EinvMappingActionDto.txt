package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvMappingActionDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("ProviderID")
    private String providerId;

    @JsonProperty("HubAction")
    private String hubAction;

    @JsonProperty("ProviderCmd")
    private String providerCmd;

    @JsonProperty("Description")
    private String description;
}