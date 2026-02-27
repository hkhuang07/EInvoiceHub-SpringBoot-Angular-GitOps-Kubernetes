package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvSyncQueueDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("InvoiceID")
    private Long invoiceId;

    @JsonProperty("CqtMessageID")
    private String cqtMessageId;

    @JsonProperty("SyncType")
    private String syncType;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("AttemptCount")
    private Integer attemptCount;

    @JsonProperty("MaxAttempts")
    private Integer maxAttempts;

    @JsonProperty("LastError")
    private String lastError;

    @JsonProperty("ErrorCode")
    private String errorCode;

    @JsonProperty("NextRetryAt")
    private LocalDateTime nextRetryAt;
}