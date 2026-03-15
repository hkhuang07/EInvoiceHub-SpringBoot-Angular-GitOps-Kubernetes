package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "einv_mapping_item_type")
public class EinvMappingItemTypeEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    @Column(name = "provider_id", length = 36)
    private String providerId;

    @Column(name = "item_type_id")
    private Byte itemTypeId;

    //ID loại hàng hóa bên NCC
    @Column(name = "provider_item_type_id", length = 36)
    private String providerItemTypeId;

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

    public Byte getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Byte itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getProviderItemTypeId() {
        return providerItemTypeId;
    }

    public void setProviderItemTypeId(String providerItemTypeId) {
        this.providerItemTypeId = providerItemTypeId;
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
