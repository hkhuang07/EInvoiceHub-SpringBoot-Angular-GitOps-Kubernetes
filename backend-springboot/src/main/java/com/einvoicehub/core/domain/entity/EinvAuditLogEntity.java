package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** nhật ký kiểm toán toàn bộ hành động trên hệ thống */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_audit_logs",
       indexes = {
           @Index(name = "idx_audit_entity", columnList = "entity_name, entity_id"),
           @Index(name = "idx_audit_action", columnList = "action"),
           @Index(name = "idx_audit_created", columnList = "created_at")
       })
public class EinvAuditLogEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action", length = 100, nullable = false)
    private String action; //SUBMIT_INVOICE, UPDATE_CONFIG

    @Column(name = "entity_name", length = 100)
    private String entityName;

    @Column(name = "entity_id", length = 100)
    private String entityId; // ID bản ghi bị tác động

    @Column(name = "payload", columnDefinition = "json")
    private String payload; // Lưu dữ liệu JSON tại thời điểm action

    @Column(name = "result", length = 20)
    private String result; // SUCCESS | FAILURE

    @Column(name = "error_msg", columnDefinition = "TEXT")
    private String errorMsg;
}