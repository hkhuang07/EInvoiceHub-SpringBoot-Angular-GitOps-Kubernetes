package vn.softz.app.einvoicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvStoreProviderDto {
    private String id;
    private String tenantId;
    private String storeId;
    private String providerId;
    private String partnerId;
    private String partnerToken;
    private String partnerUsr;
    private String partnerPwd;
    private Integer status;
    private Instant integratedDate;
    private String integrationUrl;
    private String taxCode;
}
