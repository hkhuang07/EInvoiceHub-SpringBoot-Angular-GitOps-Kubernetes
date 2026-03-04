package vn.softz.app.einvoicehub.domain.entity.mapping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "einv_mapping_item_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EinvMappingItemTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "item_type_id")
    private Integer itemTypeId;

    @Column(name = "provider_item_type_id")
    private String providerItemTypeId;

    @Column(name = "inactive")
    private Boolean inactive;

    @Column(name = "note")
    private String note;
}
