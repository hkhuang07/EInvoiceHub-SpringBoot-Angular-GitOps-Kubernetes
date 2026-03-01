package com.einvoicehub.core.domain.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * mapping: Lệnh nghiệp vụ/Hành động giữa HUB và NCC
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_mapping_action")
public class EinvMappingActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_id", length = 36, nullable = false)
    private String providerId;

    @Column(name = "hub_action", length = 50, nullable = false)
    private String hubAction;

    @Column(name = "provider_cmd", length = 100, nullable = false)
    private String providerCmd;

    @Column(name = "description", length = 255)
    private String description;

}
