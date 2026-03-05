package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "einv_mapping_payment_method")
public class EinvMappingPaymentMethod extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    @Column(name = "provider_id", length = 36)
    private String providerId;

    @Column(name = "payment_method_id")
    private Integer paymentMethodId;

    //ID phương thức thanh toán bên NCC
    @Column(name = "provider_payment_method_id", length = 36)
    private String providerPaymentMethodId;

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

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getProviderPaymentMethodId() {
        return providerPaymentMethodId;
    }

    public void setProviderPaymentMethodId(String providerPaymentMethodId) {
        this.providerPaymentMethodId = providerPaymentMethodId;
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
