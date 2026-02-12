package vn.softz.app.einvoicehub.provider.mobifone.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobifoneCancelInvoiceRequest {
    
    @JsonProperty("editmode")
    @Builder.Default
    private Integer editmode = 0;
    
    @JsonProperty("mst")
    private String mst;
    
    @JsonProperty("hdon_id")
    private String hdonId;
    
    @JsonProperty("tctbao")
    @Builder.Default
    private String tctbao = "1";
    
    @JsonProperty("ldo")
    private String ldo;
    
    @JsonProperty("khieu")
    private String khieu;
    
    @JsonProperty("shdon")
    private String shdon;
    
    @JsonProperty("nlap")
    private String nlap;
}
