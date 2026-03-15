package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "einv_tax_status")
public class EinvTaxStatusEntity extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "TINYINT")
    private Byte id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    public Byte getId() {
        return id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
