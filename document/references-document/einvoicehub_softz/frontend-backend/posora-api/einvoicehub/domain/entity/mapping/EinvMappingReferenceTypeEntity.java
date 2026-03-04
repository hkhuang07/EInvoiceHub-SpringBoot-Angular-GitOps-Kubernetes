package vn.softz.app.einvoicehub.domain.entity.mapping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "einv_mapping_reference_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EinvMappingReferenceTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "reference_type_id")
    private Integer referenceTypeId;

    @Column(name = "provider_reference_type_id")
    private String providerReferenceTypeId;

    @Column(name = "inactive")
    private Boolean inactive;

    @Column(name = "note")
    private String note;
}
