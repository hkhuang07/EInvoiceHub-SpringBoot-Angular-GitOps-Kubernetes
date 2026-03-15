package vn.softz.app.einvoicehub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvoiceHubRequest {

    @JsonProperty("tenant_id")
    @NotBlank(message = "Tenant ID is required")
    @Size(max = 36, message = "Tenant ID must not exceed 36 characters")
    private String tenantId;

    @JsonProperty("user_id")
    @Size(max = 36, message = "User ID must not exceed 36 characters")
    private String userId;

    @JsonProperty("request_id")
    @Size(max = 36, message = "Request ID must not exceed 36 characters")
    private String requestId;

    @JsonProperty("timestamp")
    private Long timestamp;
}