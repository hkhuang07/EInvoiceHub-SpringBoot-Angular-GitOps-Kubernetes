package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "einv_mapping_tax_type")
public class EinvMappingTaxTypeEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    @Column(name = "provider_id", length = 36)
    private String providerId;

    @Column(name = "tax_type_id", length = 36)
    private String taxTypeId;

    //ID loại thuế bên NCC
    @Column(name = "provider_tax_type_id", length = 36)
    private String providerTaxTypeId;

    //Tỷ lệ thuế bên NCC
    @Column(name = "provider_tax_rate", length = 36)
    private String providerTaxRate;

    @Column(name = "inactive")
    private Boolean inactive = false;

    @Column(name = "note", length = 200)
    private String note;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getTaxTypeId() {
        return taxTypeId;
    }

    public void setTaxTypeId(String taxTypeId) {
        this.taxTypeId = taxTypeId;
    }

    public String getProviderTaxTypeId() {
        return providerTaxTypeId;
    }

    public void setProviderTaxTypeId(String providerTaxTypeId) {
        this.providerTaxTypeId = providerTaxTypeId;
    }

    public String getProviderTaxRate() {
        return providerTaxRate;
    }

    public void setProviderTaxRate(String providerTaxRate) {
        this.providerTaxRate = providerTaxRate;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
