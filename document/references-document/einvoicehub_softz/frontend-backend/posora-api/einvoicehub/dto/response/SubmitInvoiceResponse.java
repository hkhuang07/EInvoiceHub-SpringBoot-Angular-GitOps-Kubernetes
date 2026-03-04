package vn.softz.app.einvoicehub.dto.response;

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
    private String invoiceId;
    
    @JsonProperty("InvoiceForm")
    private String invoiceForm;
    
    @JsonProperty("InvoiceNo")
    private String invoiceNo;
    
    @JsonProperty("InvoiceLookupCode") // mã tra cưu hd
    private String invoiceLookupCode;
    
    @JsonProperty("TaxAuthorityCode") // mã của cqt
    private String taxAuthorityCode;
    
    @JsonProperty("Provider") // mã của ncc hddt
    private String provider;
    
    @JsonProperty("ProviderInvoiceID") // id hóa đơn của ncc hddt
    private String providerInvoiceId;
    
    @JsonProperty("URLLookup") // link tra cứu của ncc hddt
    private String urlLookup;
}