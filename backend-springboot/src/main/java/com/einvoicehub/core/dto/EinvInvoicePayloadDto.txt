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
public class EinvInvoicePayloadDto {

    @JsonProperty("InvoiceID")
    private Long invoiceId;

    @JsonProperty("RequestJson")
    private String requestJson;

    @JsonProperty("RequestXml")
    private String requestXml;

    @JsonProperty("ResponseJson")
    private String responseJson;

    @JsonProperty("SignedXml")
    private String signedXml;

    @JsonProperty("PdfData")
    private String pdfData;

    @JsonProperty("ResponseRaw")
    private String responseRaw;
}