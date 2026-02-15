package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvServiceProviderDto {
    @JsonProperty("ID")
    private Long id;

    @JsonProperty("ProviderCode")
    private String providerCode; // MISA, BKAV, MOBIFONE...

    @JsonProperty("ProviderName")
    private String providerName;

    @JsonProperty("OfficialApiUrl")
    private String officialApiUrl;

    @JsonProperty("LookupUrl")
    private String lookupUrl;

    @JsonProperty("SupportEmail")
    private String supportEmail;

    @JsonProperty("SupportPhone")
    private String supportPhone;

    @JsonProperty("IsActive")
    private Boolean isActive;

    @JsonProperty("IsDefault")
    private Boolean isDefault;

    @JsonProperty("DisplayOrder")
    private Integer displayOrder;
}