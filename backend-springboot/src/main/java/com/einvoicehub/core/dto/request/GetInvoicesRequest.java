package com.einvoicehub.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetInvoicesRequest {

    @JsonProperty("InvoiceID")
    private String invoiceId;
    @JsonProperty("IdType")
    private Integer idType;
}