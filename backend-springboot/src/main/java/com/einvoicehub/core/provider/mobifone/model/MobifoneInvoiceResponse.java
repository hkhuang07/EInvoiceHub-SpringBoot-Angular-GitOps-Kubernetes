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
public class MobifoneInvoiceResponse {
    
    @JsonProperty("data")
    private MobifoneInvoiceResponseData data;
    
    @JsonProperty("ok")
    private String ok;
    
    @JsonProperty("tthai")
    private String tthai;
    
    @JsonProperty("trang_thai")
    private String trangThai;
    
    @JsonProperty("lst_hdon_id_success")
    private List<String> lstHdonIdSuccess;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("error_code")
    private String errorCode;
    
    @JsonProperty("error")
    private String error;
    
    public boolean isSuccess() {
        if (error != null && !error.isEmpty()) {
            return false;
        }
        return "true".equalsIgnoreCase(ok) || (tthai != null && !tthai.isEmpty()) || data != null;
    }
    
    public String getErrorMessage() {
        if (error != null && !error.isEmpty()) return error;
        if (message != null && !message.isEmpty()) return message;
        return null;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MobifoneInvoiceResponseData {
        
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("hdon_id")
        private String hdonId;
        
        @JsonProperty("tthai")
        private String tthai;
        
        @JsonProperty("tthdon")
        private Integer tthdon;
        
        @JsonProperty("khieu")
        private String khieu;
        
        @JsonProperty("shdon")
        private String shdon;
        
        @JsonProperty("sbmat")
        private String sbmat;
        
        @JsonProperty("mccqthue")
        private String mccqthue;
        
        @JsonProperty("tgtttbchu")
        private String tgtttbchu;
    }
}
