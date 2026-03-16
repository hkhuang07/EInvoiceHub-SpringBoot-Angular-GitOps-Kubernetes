package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "einv_mapping_invoice_type")
public class EinvMappingInvoiceTypeEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    @Column(name = "provider_id", length = 36)
    private String providerId;

    @Column(name = "invoice_type_id")
    private Byte invoiceTypeId;

    //ID loại hóa đơn bên NCC
    @Column(name = "provider_invoice_type_id", length = 36)
    private String providerInvoiceTypeId;

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

    public Byte getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(Byte invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    public String getProviderInvoiceTypeId() {
        return providerInvoiceTypeId;
    }

    public void setProviderInvoiceTypeId(String providerInvoiceTypeId) {
        this.providerInvoiceTypeId = providerInvoiceTypeId;
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
