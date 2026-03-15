package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "einv_store_provider",
        indexes = {
                @Index(name = "idx_sp_tenant",   columnList = "tenant_id"),
                @Index(name = "idx_sp_store",    columnList = "store_id"),
                @Index(name = "idx_sp_provider", columnList = "provider_id"),
                @Index(name = "idx_sp_status",   columnList = "status")
        })
public class EinvStoreProviderEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    @Column(name = "tenant_id", length = 36)
    private String tenantId;

    @Column(name = "store_id", length = 36)
    private String storeId;

    @Column(name = "provider_id", length = 36)
    private String providerId;

    @Column(name = "partner_id", length = 200)
    private String partnerId;

    @Column(name = "partner_token", length = 200)
    private String partnerToken;

    @Column(name = "partner_usr", length = 200)
    private String partnerUsr;

    @Column(name = "status", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Byte status = 0;

    @Column(name = "integrated_date")
    private LocalDateTime integratedDate;

    @Column(name = "tax_code", length = 200)
    private String taxCode;

    // Loại ký: 0=Token, 1=HSM
    @Column(name = "sign_type", columnDefinition = "TINYINT DEFAULT 0")
    private Byte signType = 0;

    @Column(name = "is_two_step_signing", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isTwoStepSigning = false;

    @Column(name = "app_id", length = 100)
    private String appId;

    @Column(name = "integration_url", length = 200)
    private String integrationUrl;

    @Column(name = "fkey_prefix", length = 50)
    private String fkeyPrefix;

    @Column(name = "partner_pwd", length = 200)
    private String partnerPwd;

    @Column(name = "username_service", length = 100)
    private String usernameService;
    @Column(name = "password_service", length = 255)
    private String passwordService;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerToken() {
        return partnerToken;
    }

    public void setPartnerToken(String partnerToken) {
        this.partnerToken = partnerToken;
    }

    public String getPartnerUsr() {
        return partnerUsr;
    }

    public void setPartnerUsr(String partnerUsr) {
        this.partnerUsr = partnerUsr;
    }

    public String getPartnerPwd() {
        return partnerPwd;
    }

    public void setPartnerPwd(String partnerPwd) {
        this.partnerPwd = partnerPwd;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public LocalDateTime getIntegratedDate() {
        return integratedDate;
    }

    public void setIntegratedDate(LocalDateTime integratedDate) {
        this.integratedDate = integratedDate;
    }

    public String getIntegrationUrl() {
        return integrationUrl;
    }

    public void setIntegrationUrl(String integrationUrl) {
        this.integrationUrl = integrationUrl;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Byte getSignType() {
        return signType;
    }

    public void setSignType(Byte signType) {
        this.signType = signType;
    }

    public Boolean getIsTwoStepSigning() {
        return isTwoStepSigning;
    }

    public void setIsTwoStepSigning(Boolean isTwoStepSigning) {
        this.isTwoStepSigning = isTwoStepSigning;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getFkeyPrefix() {
        return fkeyPrefix;
    }

    public void setFkeyPrefix(String fkeyPrefix) {
        this.fkeyPrefix = fkeyPrefix;
    }

    public String getUsernameService() {
        return usernameService;
    }

    public void setUsernameService(String usernameService) {
        this.usernameService = usernameService;
    }

    public String getPasswordService() {
        return passwordService;
    }

    public void setPasswordService(String passwordService) {
        this.passwordService = passwordService;
    }
}