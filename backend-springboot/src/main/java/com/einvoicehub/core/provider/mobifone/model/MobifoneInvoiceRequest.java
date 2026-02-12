package vn.softz.app.einvoicehub.provider.mobifone.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobifoneInvoiceRequest {
    
    @JsonProperty("editmode")
    @Builder.Default
    private Integer editmode = 1;
    
    @JsonProperty("tax_code")
    private String taxCode;
    
    @JsonProperty("data")
    private List<MobifoneInvoiceData> data;
}
