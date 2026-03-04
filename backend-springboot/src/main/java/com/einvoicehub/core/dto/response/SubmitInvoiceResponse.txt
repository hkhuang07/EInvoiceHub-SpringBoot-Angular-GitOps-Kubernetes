package com.einvoicehub.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitInvoiceResponse {

    @JsonProperty("InvoiceID")
    private String id;

    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;

    @JsonProperty("InvoiceForm")
    private String invoiceForm;

    @JsonProperty("InvoiceNo")
    private String invoiceNo;

    @JsonProperty("InvoiceSeries")
    private String invoiceSeries;

    @JsonProperty("InvoiceLookupCode")
    private String invoiceLookupCode;

    @JsonProperty("TaxAuthorityCode")
    private String taxAuthorityCode;

    @JsonProperty("Provider")
    private String provider;

    @JsonProperty("ProviderInvoiceID")
    private String providerInvoiceId;

    @JsonProperty("UrlLookup")
    private String urlLookup;

    @JsonProperty("Status")
    private Integer statusId;

    @JsonProperty("ResponseMessage")
    private String responseMessage;
}