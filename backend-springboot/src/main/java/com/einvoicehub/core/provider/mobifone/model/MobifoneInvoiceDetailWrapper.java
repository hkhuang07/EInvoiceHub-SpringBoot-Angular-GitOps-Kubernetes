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
public class MobifoneInvoiceDetailWrapper {
    
    @JsonProperty("data")
    private List<MobifoneInvoiceDetail> data;
}
