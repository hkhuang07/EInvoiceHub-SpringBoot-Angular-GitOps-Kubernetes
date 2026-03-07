package com.einvoicehub.server.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "einv_store_serial")
public class EinvStoreSerialEntity extends BaseEntity {

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

    @Column(name = "invoice_type_id")
    private Byte invoiceTypeId;

    //ID dải ký hiệu bên NCC cấp
    @Column(name = "provider_serial_id", length = 50)
    private String providerSerialId;

    //Mẫu số hóa đơn (VD: 1/001)
    @Column(name = "invoice_form", length = 20)
    private String invoiceForm;

    //Ký hiệu hóa đơn (VD: C25TAA)
    @Column(name = "invoice_serial", length = 20)
    private String invoiceSerial;

    //Ngày bắt đầu hiệu lực
    @Column(name = "start_date")
    private LocalDateTime startDate;

    //Trạng thái: 0 = Chờ duyệt, 1 = Đã duyệt, 8 = Ngưng sử dụng
    @Column(name = "status")
    private Byte status = 1;

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

    public Byte getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(Byte invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    public String getProviderSerialId() {
        return providerSerialId;
    }

    public void setProviderSerialId(String providerSerialId) {
        this.providerSerialId = providerSerialId;
    }

    public String getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(String invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public String getInvoiceSerial() {
        return invoiceSerial;
    }

    public void setInvoiceSerial(String invoiceSerial) {
        this.invoiceSerial = invoiceSerial;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Byte  getStatus() {
        return status;
    }

    public void setStatus(Byte  status) {
        this.status = status;
    }
}
