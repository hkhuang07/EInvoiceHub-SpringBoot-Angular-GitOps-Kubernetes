package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "einv_invoice_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceTypeEntity {

    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "note")
    private String note;
}
