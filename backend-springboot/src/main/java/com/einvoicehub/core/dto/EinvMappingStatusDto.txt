package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvMappingStatusDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("ProviderID")
    private String providerId;

    @JsonProperty("ProviderStatusCode")
    private String providerStatusCode;

    @JsonProperty("HubStatusID")
    private Integer hubStatusId;
}