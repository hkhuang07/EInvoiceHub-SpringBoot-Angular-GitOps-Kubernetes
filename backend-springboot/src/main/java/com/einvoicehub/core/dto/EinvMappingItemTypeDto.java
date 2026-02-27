package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvMappingItemTypeDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("ProviderID")
    private String providerId;

    @JsonProperty("InvoiceTypeID")
    private Integer invoiceTypeId;

    @JsonProperty("ProviderCode")
    private String providerCode;

    @JsonProperty("Description")
    private String description;
}