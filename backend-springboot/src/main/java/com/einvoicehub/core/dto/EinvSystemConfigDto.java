package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EinvSystemConfigDto {
    @JsonProperty("ID") private Long id;
    @JsonProperty("ConfigKey") private String configKey;
    @JsonProperty("ConfigValue") private String configValue;
    @JsonProperty("ConfigType") private String configType;
    @JsonProperty("Description") private String description;
    @JsonProperty("IsEncrypted") private Boolean isEncrypted;
    @JsonProperty("IsEditable") private Boolean isEditable;
}