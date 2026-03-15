package vn.softz.app.einvoicehub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvInvoicePayloadDto {

    @JsonProperty("invoice_id")
    @NotBlank(message = "Invoice ID is required")
    @Size(max = 36, message = "Invoice ID must not exceed 36 characters")
    private String invoiceId;

    @JsonProperty("request_json")
    private String requestJson;

    @JsonProperty("request_xml")
    private String requestXml;

    @JsonProperty("response_json")
    private String responseJson;

    @JsonProperty("signed_xml")
    private String signedXml;

    @JsonProperty("pdf_data")
    private String pdfData;

    @JsonProperty("response_raw")
    private String responseRaw;

    @JsonProperty("created_by")
    @Size(max = 36, message = "Created by must not exceed 36 characters")
    private String createdBy;

    @JsonProperty("updated_by")
    @Size(max = 36, message = "Updated by must not exceed 36 characters")
    private String updatedBy;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("updated_date")
    private LocalDateTime updatedDate;
}