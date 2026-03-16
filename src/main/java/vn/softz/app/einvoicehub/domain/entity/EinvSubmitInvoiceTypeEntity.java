package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "einv_submit_invoice_type")
public class EinvSubmitInvoiceTypeEntity extends BaseEntity {

    //Mã SubmitInvoiceType
    @Id
    @Column(name = "id", length = 3, nullable = false)
    private String id;

    //Tên nghiệp vụ
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
