package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.softz.core.entity.BaseStoreEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "einv_invoices_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EinvHubInvoiceDetailEntity extends BaseStoreEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", referencedColumnName = "id")
    private EinvHubInvoiceEntity invoice;

    @Column(name = "line_no")
    private Integer lineNo;

    @Column(name = "is_free")
    private Boolean isFree;

    @Column(name = "item_type_id")
    private Integer itemTypeId;

    @Column(name = "quantity", precision = 15, scale = 2)
    private BigDecimal quantity;

    @Column(name = "item_id", length = 36)
    private String itemId;

    @Column(name = "item_name", length = 500)
    private String itemName;

    @Column(name = "unit", length = 50)
    private String unit;

    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "gross_amount", precision = 15, scale = 2)
    private BigDecimal grossAmount;

    @Column(name = "discount_rate", precision = 15, scale = 2)
    private BigDecimal discountRate;

    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "net_price_vat", precision = 15, scale = 2)
    private BigDecimal netPriceVat;

    @Column(name = "net_price", precision = 15, scale = 2)
    private BigDecimal netPrice;

    @Column(name = "net_amount", precision = 15, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "tax_type_id", length = 36)
    private String taxTypeId;

    @Column(name = "tax_rate", precision = 15, scale = 2)
    private BigDecimal taxRate;

    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "notes", length = 500)
    private String notes;
}
