package vn.softz.app.einvoicehub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.Builder.Default;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvSyncQueueDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenant_id")
    @Size(max = 36, message = "Tenant ID must not exceed 36 characters")
    private String tenantId;

    @JsonProperty("provider_id")
    @Size(max = 36, message = "Provider ID must not exceed 36 characters")
    private String providerId;

    @JsonProperty("invoice_id")
    @Size(max = 36, message = "Invoice ID must not exceed 36 characters")
    private String invoiceId;

    @JsonProperty("cqt_message_id")
    @Size(max = 100, message = "CQT message ID must not exceed 100 characters")
    private String cqtMessageId;

    @JsonProperty("sync_type")
    @NotBlank(message = "Sync type is required")
    @Size(max = 50, message = "Sync type must not exceed 50 characters")
    private String syncType;

    @JsonProperty("status")
    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status must not exceed 20 characters")
    @Default
    private String status = "PENDING";

    @JsonProperty("attempt_count")
    @Min(value = 0, message = "Attempt count must be >= 0")
    @Default
    private Integer attemptCount = 0;

    @JsonProperty("max_attempts")
    @Min(value = 1, message = "Max attempts must be > 0")
    @Default
    private Integer maxAttempts = 3;

    @JsonProperty("last_error")
    private String lastError;

    @JsonProperty("error_code")
    @Size(max = 50, message = "Error code must not exceed 50 characters")
    private String errorCode;

    @JsonProperty("next_retry_at")
    private LocalDateTime nextRetryAt;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("updated_date")
    private LocalDateTime updatedDate;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_by")
    private String updateBy;

}