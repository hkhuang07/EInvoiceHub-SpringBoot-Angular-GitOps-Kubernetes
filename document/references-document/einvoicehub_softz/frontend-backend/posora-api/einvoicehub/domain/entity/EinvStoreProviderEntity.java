package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.softz.core.entity.BaseStoreEntity;

import java.time.Instant;

@Entity
@Table(name = "einv_store_provider")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EinvStoreProviderEntity extends BaseStoreEntity {

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "partner_id")
    private String partnerId;

    @Column(name = "partner_token")
    private String partnerToken;

    @Column(name = "partner_usr")
    private String partnerUsr;

    @Column(name = "partner_pwd")
    private String partnerPwd;

    @Column(name = "status")
    private Integer status;

    @Column(name = "integrated_date")
    private Instant integratedDate;

    @Column(name = "integration_url")
    private String integrationUrl;

    @Column(name = "tax_code")
    private String taxCode;
}
