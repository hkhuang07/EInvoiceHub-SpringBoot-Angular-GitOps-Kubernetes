package com.einvoicehub.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/** danh mục Trạng thái hóa đơn */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_invoice_status")
public class EinvInvoiceStatusEntity extends BaseAuditEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id; // Mã trạng thái hệ thống HUB

    @Column(name = "name", length = 100, nullable = false)
    private String name; // Tên trạng thái

    @Column(name = "description", length = 500)
    private String description; // Mô tả chi tiết

    @Column(name = "note", length = 500)
    private String note;
}