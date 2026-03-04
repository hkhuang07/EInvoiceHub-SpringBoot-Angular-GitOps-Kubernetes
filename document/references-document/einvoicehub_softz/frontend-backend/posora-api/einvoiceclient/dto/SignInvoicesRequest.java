package vn.softz.app.einvoiceclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInvoicesRequest {
    @JsonProperty("IDType")
    private Integer idType;

    @JsonProperty("InvoiceID")
    private String invoiceId;
}
