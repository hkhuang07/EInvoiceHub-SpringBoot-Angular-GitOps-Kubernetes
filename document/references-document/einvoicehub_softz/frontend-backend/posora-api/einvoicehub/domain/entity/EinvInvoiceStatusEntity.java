package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "einv_invoice_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceStatusEntity {

    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "note")
    private String note;
}
