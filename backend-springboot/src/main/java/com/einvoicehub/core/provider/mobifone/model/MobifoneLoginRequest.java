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
public class MobifoneLoginRequest {
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("tax_code")
    private String taxCode;
}
