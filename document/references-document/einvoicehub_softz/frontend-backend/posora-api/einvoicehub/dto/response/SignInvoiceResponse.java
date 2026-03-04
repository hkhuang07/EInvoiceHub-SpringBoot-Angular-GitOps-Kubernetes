package vn.softz.app.einvoicehub.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInvoiceResponse {
    
    @JsonProperty("PartnerInvoiceID")
    private String partnerInvoiceId;
    
    @JsonProperty("InvoiceID")
    private String invoiceId;
    
    @JsonProperty("SignedDate")
    private String signedDate;
}