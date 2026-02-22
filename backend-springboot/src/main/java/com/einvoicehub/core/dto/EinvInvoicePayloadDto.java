package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoicePayloadDto {
    @JsonProperty("InvoiceID")
    private Long invoiceId;

    @JsonProperty("RawData")
    private String rawData;

    @JsonProperty("XmlContent")
    private String xmlContent;

    @JsonProperty("JsonContent")
    private String jsonContent;

    @JsonProperty("SignedXml")
    private String signedXml;

    @JsonProperty("PdfData")
    private String pdfData;

    @JsonProperty("ExtraData")
    private String extraData;
}