package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "einv_mapping_invoice_status")
public class EinvMappingInvoiceStatus extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    @Column(name = "provider_id", length = 36)
    private String providerId;

    @Column(name = "invoice_status_id")
    private Integer invoiceStatusId;

    //ID trạng thái bên NCC
    @Column(name = "provider_invoice_status_id", length = 36)
    private String providerInvoiceStatusId;

    //Trạng thái active: 0 = Mặc định, 1 = Disable
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

    public Integer getInvoiceStatusId() {
        return invoiceStatusId;
    }

    public void setInvoiceStatusId(Integer invoiceStatusId) {
        this.invoiceStatusId = invoiceStatusId;
    }

    public String getProviderInvoiceStatusId() {
        return providerInvoiceStatusId;
    }

    public void setProviderInvoiceStatusId(String providerInvoiceStatusId) {
        this.providerInvoiceStatusId = providerInvoiceStatusId;
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
