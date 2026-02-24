package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Entity
@Table(name = "einv_invoice_payloads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = "invoice")
public class EinvInvoicePayloadEntity {

    @Id
    @Column(name = "invoice_id")
    private Long invoiceId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "invoice_id")
    private EinvInvoiceEntity invoice;

    @Lob
    @Column(name = "request_json", columnDefinition = "LONGTEXT")
    private String requestJson; //JSON record send to ServiceProvider

    @Lob
    @Column(name = "request_xml", columnDefinition = "LONGTEXT")
    private String requestXml; // XML Record

    @Lob
    @Column(name = "response_json", columnDefinition = "LONGTEXT")
    private String responseJson;

    @Lob
    @Column(name = "signed_xml", columnDefinition = "LONGTEXT")
    private String signedXml; // XML digital signed

    @Lob
    @Column(name = "pdf_data", columnDefinition = "LONGTEXT")
    private String pdfData; // PDF base64
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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