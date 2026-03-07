package com.einvoicehub.server.dto.response;

import com.einvoicehub.server.dto.EinvInvoiceDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**Response cho API GetInvoices — lấy thông tin đầy đủ một hóa đơn*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetInvoicesResponse {

    @JsonProperty("invoice")
    private EinvInvoiceDto invoice;

    @JsonProperty("invoice_lookup_code")
    private String invoiceLookupCode;

    /**Link tra cứu = einv_provider.lookup_url + einv_invoices.invoice_lookup_code*/
    @JsonProperty("url_lookup")
    private String urlLookup;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("message")
    private String message;

    @JsonProperty("error_code")
    private String errorCode;

    public static GetInvoicesResponse success(EinvInvoiceDto invoice, String urlLookup, String provider) {
        return GetInvoicesResponse.builder()
                .invoice(invoice)
                .invoiceLookupCode(invoice != null ? invoice.getInvoiceLookupCode() : null)
                .urlLookup(urlLookup)
                .provider(provider)
                .build();
    }

    public static GetInvoicesResponse notFound(String message) {
        return GetInvoicesResponse.builder()
                .message(message)
                .errorCode("INVOICE_NOT_FOUND")
                .build();
    }

    public static GetInvoicesResponse error(String errorCode, String message) {
        return GetInvoicesResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .build();
    }
}