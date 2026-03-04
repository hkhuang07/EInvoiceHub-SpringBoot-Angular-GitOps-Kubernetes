package vn.softz.app.einvoicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvValidationResult {
    private boolean success;
    private String message;
    
    public static EinvValidationResult ok(String message) {
        return EinvValidationResult.builder().success(true).message(message).build();
    }
    
    public static EinvValidationResult fail(String message) {
        return EinvValidationResult.builder().success(false).message(message).build();
    }
}
