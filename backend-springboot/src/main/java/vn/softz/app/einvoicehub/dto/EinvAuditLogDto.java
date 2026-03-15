package vn.softz.app.einvoicehub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvAuditLogDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("action")
    @NotBlank(message = "Action is required")
    @Size(max = 100, message = "Action must not exceed 100 characters")
    private String action;

    @JsonProperty("entity_name")
    @Size(max = 100, message = "Entity name must not exceed 100 characters")
    private String entityName;

    @JsonProperty("entity_id")
    @Size(max = 100, message = "Entity ID must not exceed 100 characters")
    private String entityId;

    @JsonProperty("payload")
    private JsonNode payload;

    @JsonProperty("result")
    @Size(max = 20, message = "Result must not exceed 20 characters")
    private String result;

    @JsonProperty("error_msg")
    private String errorMsg;

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