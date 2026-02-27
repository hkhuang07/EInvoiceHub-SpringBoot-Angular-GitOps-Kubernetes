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
public class EinvInvoiceTypeDto {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("TypeName")
    private String invoiceTypeName;

    @JsonProperty("SortOrder")
    private Integer sortOrder;

    @JsonProperty("Note")
    private String note;
}