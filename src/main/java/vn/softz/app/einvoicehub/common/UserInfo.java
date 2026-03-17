package vn.softz.app.einvoicehub.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private String userId;

    private String username;

    private String fullName;

    private String tenantId;

    private String locId;

    private String email;

    private java.util.List<String> roles;

    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
    
    public String getStoreId() {
        return locId;
    }
}
