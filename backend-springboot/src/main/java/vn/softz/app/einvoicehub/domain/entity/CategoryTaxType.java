package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "category_tax_type")
public class CategoryTaxType extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    @Column(name = "tax_name", length = 100)
    private String taxName;

    @Column(name = "tax_name_en", length = 100)
    private String taxNameEn;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "vat", precision = 15, scale = 2)
    private BigDecimal vat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxNameEn() {
        return taxNameEn;
    }

    public void setTaxNameEn(String taxNameEn) {
        this.taxNameEn = taxNameEn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }
}
