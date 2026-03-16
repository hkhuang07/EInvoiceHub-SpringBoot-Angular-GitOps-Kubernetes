package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "einv_audit_logs",
       indexes = {
           @Index(name = "idx_audit_entity", columnList = "entity_name, entity_id"),
           @Index(name = "idx_audit_action", columnList = "action"),
           @Index(name = "idx_audit_created", columnList = "created_date")
       })
public class EinvAuditLogEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /*@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false)
    protected String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    */

    @Column(name = "action", length = 100, nullable = false)
    private String action;

    @Column(name = "entity_name", length = 100)
    private String entityName;

    @Column(name = "entity_id", length = 100)
    private String entityId;

    @Column(name = "payload", columnDefinition = "JSON")
    private String payload;

    @Column(name = "result", length = 20)
    private String result;

    @Column(name = "error_msg", columnDefinition = "TEXT")
    private String errorMsg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
