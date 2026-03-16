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
public class EinvPartnerTokenDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("partner_id")
    @NotBlank(message = "Partner ID is required")
    @Size(max = 200, message = "Partner ID must not exceed 200 characters")
    private String partnerId;

    @JsonProperty("token")
    @NotBlank(message = "Token is required")
    @Size(max = 200, message = "Token must not exceed 200 characters")
    private String token;

    @JsonProperty("is_active")
    @NotNull(message = "Active status is required")
    @Default
    private Boolean isActive = true;

    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("update_by")
    private String updateBy;
}