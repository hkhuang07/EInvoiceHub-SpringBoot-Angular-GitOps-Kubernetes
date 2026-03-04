package vn.softz.app.einvoicehub.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.softz.core.entity.BaseStoreEntity;

import java.time.Instant;

@Entity
@Table(name = "einv_store_serial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EinvStoreSerialEntity extends BaseStoreEntity {

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "invoice_type_id")
    private Integer invoiceTypeId;

    @Column(name = "provider_serial_id")
    private String providerSerialId;

    @Column(name = "invoice_form")
    private String invoiceForm;

    @Column(name = "invoice_serial")
    private String invoiceSerial;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "status")
    private Integer status;
}
