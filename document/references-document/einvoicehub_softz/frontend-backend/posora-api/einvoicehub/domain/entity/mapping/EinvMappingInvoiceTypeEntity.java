package vn.softz.app.einvoicehub.domain.entity.mapping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "einv_mapping_invoice_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EinvMappingInvoiceTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "invoice_type_id")
    private Integer invoiceTypeId;

    @Column(name = "provider_invoice_type_id")
    private String providerInvoiceTypeId;

    @Column(name = "inactive")
    private Boolean inactive;

    @Column(name = "note")
    private String note;
}
