package vn.softz.app.einvoicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvStoreProviderRequest {
    private String providerId;

    // BKAV fields
    private String bkavPartnerGuid;
    private String bkavPartnerToken;

    // MobiFone fields
    private String mobifoneUsername;
    private String mobifonePassword;
    private String mobifoneTaxCode;

    // dành cho vpnt thì set
    private String integrationUrl;
}
