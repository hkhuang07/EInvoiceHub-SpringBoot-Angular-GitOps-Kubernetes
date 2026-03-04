package vn.softz.app.einvoicehub.domain.entity.mapping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "einv_mapping_invoice_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EinvMappingInvoiceStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "invoice_status_id")
    private Integer invoiceStatusId;

    @Column(name = "provider_invoice_status_id")
    private String providerInvoiceStatusId;

    @Column(name = "inactive")
    private Boolean inactive;

    @Column(name = "note")
    private String note;
}
