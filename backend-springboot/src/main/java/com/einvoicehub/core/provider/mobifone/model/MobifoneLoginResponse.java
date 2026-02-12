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
public class MobifoneLoginResponse {
    
    @JsonProperty("isWeakPass")
    private Boolean isWeakPass;
    
    @JsonProperty("token")
    private String token;
    
    @JsonProperty("refresh_token")
    private String refreshToken;
    
    @JsonProperty("ma_dvcs")
    private String maDvcs;
    
    @JsonProperty("wb_user_id")
    private String wbUserId;
    
    @JsonProperty("noti_sso")
    private String notiSso;
    
    @JsonProperty("notes_sso")
    private String notesSso;
    
    @JsonProperty("notify")
    private String notify;
    
    @JsonProperty("is_link_sso")
    private Boolean isLinkSso;
}
