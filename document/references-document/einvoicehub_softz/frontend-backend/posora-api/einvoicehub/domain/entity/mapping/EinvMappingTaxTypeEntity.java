package vn.softz.app.einvoicehub.domain.entity.mapping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "einv_mapping_tax_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EinvMappingTaxTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "tax_type_id")
    private String taxTypeId;

    @Column(name = "provider_tax_type_id")
    private String providerTaxTypeId;

    @Column(name = "provider_tax_rate")
    private String providerTaxRate;

    @Column(name = "inactive")
    private Boolean inactive;

    @Column(name = "note")
    private String note;
}
