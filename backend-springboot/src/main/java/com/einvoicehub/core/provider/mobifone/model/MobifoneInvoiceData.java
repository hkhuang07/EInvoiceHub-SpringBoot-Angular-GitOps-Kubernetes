package vn.softz.app.einvoicehub.provider.mobifone.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobifoneInvoiceData {
    
    @JsonProperty("cctbao_id")
    private String cctbaoId;
    
    @JsonProperty("nlap")
    private String nlap;
    
    @JsonProperty("dvtte")
    @Builder.Default
    private String dvtte = "VND";
    
    @JsonProperty("tgia")
    @Builder.Default
    private BigDecimal tgia = BigDecimal.ONE;
    
    @JsonProperty("htttoan")
    private String htttoan;
    
    @JsonProperty("mdvi")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String mdvi;
    
    @JsonProperty("is_hdcma")
    @Builder.Default
    private Integer isHdcma = 1;
    
    // Buyer
    @JsonProperty("mnmua")
    private String mnmua;
    
    @JsonProperty("mst")
    private String mst;
    
    @JsonProperty("tnmua")
    private String tnmua;
    
    @JsonProperty("ten")
    private String ten;
    
    @JsonProperty("dchi")
    private String dchi;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("sdtnmua")
    private String sdtnmua;
    
    @JsonProperty("stknmua")
    private String stknmua;
    
    @JsonProperty("tnhmua")
    private String tnhmua;
    
    @JsonProperty("cmndmua")
    private String cmndmua;
    
    // Seller
    @JsonProperty("sdtnban")
    private String sdtnban;
    
    @JsonProperty("stknban")
    private String stknban;
    
    @JsonProperty("tnhban")
    private String tnhban;
    
    // Amounts
    @JsonProperty("tgtcthue")
    private BigDecimal tgtcthue;
    
    @JsonProperty("tgtthue")
    private BigDecimal tgtthue;
    
    @JsonProperty("tgtttbso")
    private BigDecimal tgtttbso;
    
    @JsonProperty("tgtttbso_last")
    private BigDecimal tgtttbsoLast;
    
    @JsonProperty("TGTKCThue")
    private BigDecimal tgtkcthue;
    
    @JsonProperty("tkcktmn")
    private BigDecimal tkcktmn;
    
    @JsonProperty("TGTKhac")
    private BigDecimal tgtkhac;
    
    @JsonProperty("tgtphi")
    private BigDecimal tgtphi;
    
    // Status & Result
    @JsonProperty("hdon_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String hdonId;
    
    @JsonProperty("khieu")
    private String khieu;
    
    @JsonProperty("shdon")
    private String shdon;
    
    @JsonProperty("sdhang")
    private String sdhang;
    
    @JsonProperty("tthdon")
    private Integer tthdon;
    
    @JsonProperty("tthai")
    private String tthai;
    
    @JsonProperty("sbmat")
    private String sbmat;
    
    @JsonProperty("mccqthue")
    private String mccqthue;
    
    // Replacement/Adjustment
    @JsonProperty("hdon_id_old")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String hdonIdOld;
    
    @JsonProperty("lhdclquan")
    private Integer lhdclquan;
    
    @JsonProperty("khmshdclquan")
    private String khmshdclquan;
    
    @JsonProperty("khhdclquan")
    private String khhdclquan;
    
    @JsonProperty("shdclquan")
    private String shdclquan;
    
    @JsonProperty("nlhdclquan")
    private String nlhdclquan;
    
    // Store
    @JsonProperty("tchang")
    private String tchang;
    
    @JsonProperty("mchang")
    private String mchang;
    
    // Details
    @JsonProperty("details")
    private List<MobifoneInvoiceDetailWrapper> details;
}
