package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "einv_mapping_reference_type")
public class EinvMappingReferenceTypeEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    @Column(name = "provider_id", length = 36)
    private String providerId;

    @Column(name = "reference_type_id")
    private Byte referenceTypeId;

    //ID loại tham chiếu bên NCC
    @Column(name = "provider_reference_type_id", length = 36)
    private String providerReferenceTypeId;

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

    public Byte getReferenceTypeId() {
        return referenceTypeId;
    }

    public void setReferenceTypeId(Byte referenceTypeId) {
        this.referenceTypeId = referenceTypeId;
    }

    public String getProviderReferenceTypeId() {
        return providerReferenceTypeId;
    }

    public void setProviderReferenceTypeId(String providerReferenceTypeId) {
        this.providerReferenceTypeId = providerReferenceTypeId;
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
