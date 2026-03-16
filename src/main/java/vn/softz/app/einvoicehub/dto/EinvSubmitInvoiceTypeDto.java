package vn.softz.app.einvoicehub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinvSubmitInvoiceTypeDto {

    @JsonProperty("id")
    @NotBlank(message = "Submit invoice type ID is required")
    @Size(max = 3, message = "ID must not exceed 3 characters")
    private String id;

    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @JsonProperty("description")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_by")
    private String updateBy;
}