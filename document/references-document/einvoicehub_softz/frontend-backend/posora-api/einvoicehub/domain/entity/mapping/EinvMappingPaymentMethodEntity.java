package vn.softz.app.einvoicehub.domain.entity.mapping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "einv_mapping_payment_method")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EinvMappingPaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "payment_method_id")
    private Integer paymentMethodId;

    @Column(name = "provider_payment_method_id")
    private String providerPaymentMethodId;

    @Column(name = "inactive")
    private Boolean inactive;

    @Column(name = "note")
    private String note;
}
