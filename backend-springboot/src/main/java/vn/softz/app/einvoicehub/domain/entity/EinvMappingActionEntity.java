package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "einv_mapping_action")
public class EinvMappingActionEntity extends BaseEntity {

    /*@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "provider_id", length = 36, nullable = false)
    private String providerId;

    @Column(name = "hub_action", length = 50, nullable = false)
    private String hubAction;

    @Column(name = "provider_cmd", length = 100, nullable = false)
    private String providerCmd;

    @Column(name = "description", length = 255)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getHubAction() {
        return hubAction;
    }

    public void setHubAction(String hubAction) {
        this.hubAction = hubAction;
    }

    public String getProviderCmd() {
        return providerCmd;
    }

    public void setProviderCmd(String providerCmd) {
        this.providerCmd = providerCmd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
