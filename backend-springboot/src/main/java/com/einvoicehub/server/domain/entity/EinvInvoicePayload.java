package com.einvoicehub.server.domain.entity;

import com.einvoicehub.server.config.JsonConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "einv_invoice_payloads")
public class EinvInvoicePayload extends BaseEntity {

    @Id
    @Column(name = "invoice_id", length = 36, nullable = false)
    private String invoiceId;

    @Column(name = "request_json", columnDefinition = "LONGTEXT")
    private String requestJson;

    @Column(name = "request_xml", columnDefinition = "LONGTEXT")
    private String requestXml;

    @Column(name = "response_json", columnDefinition = "LONGTEXT")
    private String responseJson;

    @Column(name = "signed_xml", columnDefinition = "LONGTEXT")
    private String signedXml;

    @Column(name = "pdf_data", columnDefinition = "LONGTEXT")
    private String pdfData;

    @Column(name = "response_raw", columnDefinition = "LONGTEXT")
    private String responseRaw;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public String getRequestXml() {
        return requestXml;
    }

    public void setRequestXml(String requestXml) {
        this.requestXml = requestXml;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    public String getSignedXml() {
        return signedXml;
    }

    public void setSignedXml(String signedXml) {
        this.signedXml = signedXml;
    }

    public String getPdfData() {
        return pdfData;
    }

    public void setPdfData(String pdfData) {
        this.pdfData = pdfData;
    }

    public String getResponseRaw() {
        return responseRaw;
    }

    public void setResponseRaw(String responseRaw) {
        this.responseRaw = responseRaw;
    }
}
