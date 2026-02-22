package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EinvTaxAuthorityResponseDto {
    @JsonProperty("ID") private Long id;
    @JsonProperty("InvoiceID") private Long invoiceId;
    @JsonProperty("CqtCode") private String cqtCode;
    @JsonProperty("StatusFromCqt") private String statusFromCqt;
    @JsonProperty("ProcessingCode") private String processingCode;
    @JsonProperty("SignatureData") private String signatureData;
    @JsonProperty("RawResponse") private String rawResponse;
    @JsonProperty("ErrorCode") private String errorCode;
    @JsonProperty("ErrorMessage") private String errorMessage;
    @JsonProperty("ReceivedAt") private LocalDateTime receivedAt;
}