package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_payloads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "invoice")
public class EinvInvoicePayloadEntity {

    @Id
    @Column(name = "invoice_id")
    private Long invoiceId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "invoice_id")
    private EinvInvoiceMetadataEntity invoice;

    @Column(name = "raw_data", columnDefinition = "JSON")
    private String rawData;

    @Column(name = "xml_content", columnDefinition = "LONGTEXT")
    private String xmlContent;

    @Column(name = "json_content", columnDefinition = "LONGTEXT")
    private String jsonContent;

    @Column(name = "signed_xml", columnDefinition = "LONGTEXT")
    private String signedXml;

    @Column(name = "pdf_data", columnDefinition = "LONGTEXT")
    private String pdfData;

    @Column(name = "extra_data", columnDefinition = "JSON")
    private String extraData;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    // --------------------------------

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}