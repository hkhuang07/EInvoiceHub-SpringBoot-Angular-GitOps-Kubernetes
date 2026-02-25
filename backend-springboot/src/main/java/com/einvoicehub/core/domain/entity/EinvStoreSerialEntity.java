package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

/** 1uản lý Dải ký hiệu/mẫu số hóa đơn theo Store */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_store_serial",
        indexes = { @Index(name = "idx_ss_store_provider", columnList = "store_id, provider_id, status") })
public class EinvStoreSerialEntity extends TenantEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id; // UUID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ss_store"))
    private EinvStoreEntity store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ss_provider"))
    private EinvProviderEntity provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_type_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ss_invoice_type"))
    private EinvInvoiceTypeEntity invoiceType;

    @Column(name = "provider_serial_id", length = 100)
    private String providerSerialId;

    @Column(name = "invoice_form", length = 50)
    private String invoiceForm;

    @Column(name = "invoice_serial", length = 20)
    private String invoiceSerial;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "status", nullable = false)
    private Integer status = 1; // 1=Active 0=Inactive
}