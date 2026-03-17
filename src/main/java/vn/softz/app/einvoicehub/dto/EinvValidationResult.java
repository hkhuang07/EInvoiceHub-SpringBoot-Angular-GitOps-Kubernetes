package vn.softz.app.einvoicehub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvValidationResult {

    @JsonProperty("valid")
    private Boolean valid;

    @JsonProperty("errors")
    @Builder.Default
    private List<ValidationError> errors = new ArrayList<>();

    public static EinvValidationResult success() {
        return EinvValidationResult.builder().valid(true).build();
    }

    public static EinvValidationResult success(String message) {
        return EinvValidationResult.builder().valid(true).build();
    }

    public boolean isSuccess() {
        return Boolean.TRUE.equals(this.valid);
    }

    public static EinvValidationResult error(String field, String message) {
        EinvValidationResult result = EinvValidationResult.builder().valid(false).errors(new ArrayList<>()).build();
        result.addError(field, message);
        return result;
    }

    public static EinvValidationResult errors(List<ValidationError> errors) {
        return EinvValidationResult.builder()
                .valid(false)
                .errors(errors)
                .build();
    }

    public void addError(String field, String message) {
        if (this.errors == null) this.errors = new ArrayList<>();
        this.errors.add(new ValidationError(field, message));
        this.valid = false;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ValidationError {

        @JsonProperty("field")
        @NotBlank(message = "Field name is required")
        @Size(max = 100, message = "Field name must not exceed 100 characters")
        private String field;

        @JsonProperty("message")
        @NotBlank(message = "Error message is required")
        @Size(max = 500, message = "Error message must not exceed 500 characters")
        private String message;
    }
}