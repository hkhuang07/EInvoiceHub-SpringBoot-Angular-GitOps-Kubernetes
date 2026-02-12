package vn.softz.app.einvoicehub.provider.mobifone.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobifoneInvoiceDetail {
    
    @JsonProperty("stt")
    private Integer stt;
    
    @JsonProperty("ma")
    private String ma;
    
    @JsonProperty("ten")
    private String ten;
    
    @JsonProperty("mdvtinh")
    private String mdvtinh;
    
    @JsonProperty("dvtinh")
    private String dvtinh;
    
    @JsonProperty("sluong")
    private BigDecimal sluong;
    
    @JsonProperty("dgia")
    private BigDecimal dgia;
    
    @JsonProperty("thtien")
    private BigDecimal thtien;
    
    @JsonProperty("tlckhau")
    private BigDecimal tlckhau;
    
    @JsonProperty("stckhau")
    private BigDecimal stckhau;
    
    @JsonProperty("tsuat")
    private String tsuat;
    
    @JsonProperty("tthue")
    private BigDecimal tthue;
    
    @JsonProperty("tgtien")
    private BigDecimal tgtien;
    
    @JsonProperty("kmai")
    private Integer kmai;
    
    @JsonProperty("lhhdthu")
    private Integer lhhdthu;
    
    @JsonProperty("skhung")
    private String skhung;
    
    @JsonProperty("smay")
    private String smay;
}
