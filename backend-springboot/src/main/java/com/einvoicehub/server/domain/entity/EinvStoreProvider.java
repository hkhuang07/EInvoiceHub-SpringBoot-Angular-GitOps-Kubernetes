package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "einv_store_provider")
public class EinvStoreProvider extends BaseEntity {

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

    //PartnerGUID hoặc vnpt_Account
    @Column(name = "partner_id", length = 200)
    private String partnerId;

    //PartnerToken hoặc vnpt_ACPass
    @Column(name = "partner_token", length = 200)
    private String partnerToken;

    //MISA: app_id, vnpt_username
    @Column(name = "partner_usr", length = 200)
    @Transient
    private String partnerUsr;

    //vnpt_password
    @Column(name = "partner_pwd", length = 200)
    @Transient
    private String partnerPwd;

    //Trạng thái tích hợp: 0 = Chưa tích hợp, 1 = Tích hợp thành công, 8 = Đã hủy tích hợp
    @Column(name = "status")
    private Boolean status = false;

    //Ngày tích hợp thành công
    @Column(name = "integrated_date")
    private LocalDateTime integratedDate;

    //Override URL riêng cho VNPT
    @Column(name = "integration_url", length = 200)
    private String integrationUrl;

    @Column(name = "tax_code", length = 200)
    private String taxCode;

    //Loại ký: 0 = Token, 1 = HSM
    @Column(name = "sign_type")
    private Integer signType = 0;

    @Column(name = "is_two_step_signing")
    private Boolean isTwoStepSigning = false;

    //for MISA
    @Column(name = "app_id", length = 100)
    private String appId;

    //Tiền tố Fkey for VNPT
    @Column(name = "fkey_prefix", length = 50)
    private String fkeyPrefix;

    //User API - Pass API for Mobifone
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
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

    public Integer getSignType() {
        return signType;
    }

    public void setSignType(Integer signType) {
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
