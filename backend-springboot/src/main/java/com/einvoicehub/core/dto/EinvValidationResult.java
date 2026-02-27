package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvValidationResult {

    @Builder.Default
    @JsonProperty("Success")
    private boolean success = true;

    @JsonProperty("Message")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("FieldErrors")
    private Map<String, String> fieldErrors = new HashMap<>();

    public static EinvValidationResult ok(String message) {
        return EinvValidationResult.builder()
                .success(true)
                .message(message)
                .build();
    }

    public static EinvValidationResult fail(String message) {
        return EinvValidationResult.builder()
                .success(false)
                .message(message)
                .build();
    }

    public void addError(String field, String error) {
        this.success = false;
        if (this.fieldErrors == null) this.fieldErrors = new HashMap<>();
        this.fieldErrors.put(field, error);
    }
}

