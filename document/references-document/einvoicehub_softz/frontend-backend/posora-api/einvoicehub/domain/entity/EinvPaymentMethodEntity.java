package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "einv_payment_method")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EinvPaymentMethodEntity {

    @Id
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "note")
    private String note;
}
