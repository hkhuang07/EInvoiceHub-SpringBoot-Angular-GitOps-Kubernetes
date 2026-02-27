package com.einvoicehub.core.dto.response;

import com.einvoicehub.core.dto.EinvInvoiceDto;
import com.einvoicehub.core.dto.EinvInvoiceDetailDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetInvoicesResponse {

    @JsonProperty("InvoiceHeader")
    private EinvInvoiceDto invoiceHeader;

    @JsonProperty("InvoiceDetails")
    private List<EinvInvoiceDetailDto> invoiceDetails;
}