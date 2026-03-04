package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** lưu trữ Payload reponse-request */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "einv_invoice_payloads")
public class EinvInvoicePayloadEntity extends BaseAuditEntity {

    @Id
    @Column(name = "invoice_id")
    private Long invoiceId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "invoice_id", foreignKey = @ForeignKey(name = "fk_payload_invoice"))
    private EinvInvoiceEntity invoice;

    @Lob
    @Column(name = "request_json", columnDefinition = "LONGTEXT")
    private String requestJson;

    @Lob
    @Column(name = "request_xml", columnDefinition = "LONGTEXT")
    private String requestXml;

    @Lob
    @Column(name = "response_json", columnDefinition = "LONGTEXT")
    private String responseJson;

    @Lob
    @Column(name = "signed_xml", columnDefinition = "LONGTEXT")
    private String signedXml;

    @Lob
    @Column(name = "pdf_data", columnDefinition = "LONGTEXT")
    private String pdfData;

    @Lob
    @Column(name = "response_raw", columnDefinition = "LONGTEXT")
    private String responseRaw;
}