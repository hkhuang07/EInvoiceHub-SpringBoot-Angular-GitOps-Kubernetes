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
public class EinvAuditLogDto {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("Action")
    private String action;

    @JsonProperty("EntityName")
    private String entityName;

    @JsonProperty("EntityID")
    private String entityId;

    @JsonProperty("Payload")
    private String payload;

    @JsonProperty("Result")
    private String result;

    @JsonProperty("ErrorMsg")
    private String errorMsg;

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("CreatedAt")
    private LocalDateTime createdAt;
}