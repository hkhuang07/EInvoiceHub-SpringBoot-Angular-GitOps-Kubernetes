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
public class EinvMappingUnitDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("ProviderID")
    private String providerId;

    @JsonProperty("SystemCode")
    private String systemCode;

    @JsonProperty("ProviderCode")
    private String providerCode;
}