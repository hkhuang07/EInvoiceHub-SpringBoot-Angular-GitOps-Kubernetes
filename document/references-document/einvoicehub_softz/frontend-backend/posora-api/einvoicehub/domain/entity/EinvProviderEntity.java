package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "einv_provider")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EinvProviderEntity {

    @Id
    private String id;

    @Column(name = "provider_name")
    private String providerName;

    @Column(name = "integration_url")
    private String integrationUrl;

    @Column(name = "lookup_url")
    private String lookupUrl;

    @Column(name = "inactive")
    private Boolean inactive;
}
