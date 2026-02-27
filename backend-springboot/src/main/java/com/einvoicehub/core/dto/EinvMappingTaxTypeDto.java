package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvMappingTaxTypeDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("ProviderID")
    private String providerId;

    @JsonProperty("SystemCode")
    private String systemCode;

    @JsonProperty("ProviderCode")
    private String providerCode;

    @JsonProperty("Description")
    private String description;
}