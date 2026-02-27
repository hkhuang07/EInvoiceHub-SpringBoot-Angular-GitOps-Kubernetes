package com.einvoicehub.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetInvoicesRequest {

    /** * 0: InvoiceID (Hub ID)
        * 1: PartnerInvoiceID (POS ID)*/
    @JsonProperty("IDType")
    private Integer idType = 0;

    @JsonProperty("InvoiceID")
    private String invoiceId;
}