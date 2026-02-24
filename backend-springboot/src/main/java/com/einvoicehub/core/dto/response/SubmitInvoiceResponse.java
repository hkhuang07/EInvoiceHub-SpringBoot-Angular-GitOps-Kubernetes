package com.einvoicehub.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitInvoiceResponse {

    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;

    @JsonProperty("InvoiceID")
    private Long invoiceId;

    @JsonProperty("InvoiceNumber")
    private String invoiceNumber;

    @JsonProperty("LookupCode")
    private String lookupCode;

    @JsonProperty("TaxAuthorityCode")
    private String taxAuthorityCode;

    @JsonProperty("URLLookup")
    private String urlLookup;

    @JsonProperty("ProviderInvoiceID")
    private String providerInvoiceId;
}