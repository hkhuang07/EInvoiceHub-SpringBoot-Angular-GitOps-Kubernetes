package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvProviderDto {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("ProviderCode")
    private String providerCode;

    @JsonProperty("ProviderName")
    private String providerName;

    @JsonProperty("IntegrationUrl")
    private String integrationUrl;

    @JsonProperty("UrlLookup")
    private String urlLookup;

    @JsonProperty("IntegrationType")
    private Integer integrationType;

    @JsonProperty("IsInactive")
    private Boolean isInactive;

    @JsonProperty("CreatedAt")
    private LocalDateTime createdAt;
}