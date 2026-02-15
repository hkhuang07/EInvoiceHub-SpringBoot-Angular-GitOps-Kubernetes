package com.einvoicehub.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvSyncQueueResponse {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("InvoiceID")
    private Long invoiceId;

    @JsonProperty("InvoiceNumber")
    private String invoiceNumber;

    @JsonProperty("SyncType")
    private String syncType;

    @JsonProperty("Priority")
    private Integer priority;

    @JsonProperty("Status")
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED, RETRYING

    @JsonProperty("AttemptCount")
    private Integer attemptCount;

    @JsonProperty("MaxAttempts")
    private Integer maxAttempts;

    @JsonProperty("LastAttemptAt")
    private String lastAttemptAt;

    @JsonProperty("NextRetryAt")
    private String nextRetryAt;

    @JsonProperty("RequestData")
    private String requestData;

    @JsonProperty("ResponseData")
    private String responseData;

    @JsonProperty("ErrorCode")
    private String errorCode;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("ProcessedBy")
    private String processedBy;

    @JsonProperty("ProcessingStartedAt")
    private String processingStartedAt;

    @JsonProperty("ProcessingCompletedAt")
    private String processingCompletedAt;

    @JsonProperty("CreatedAt")
    private String createdAt;
}