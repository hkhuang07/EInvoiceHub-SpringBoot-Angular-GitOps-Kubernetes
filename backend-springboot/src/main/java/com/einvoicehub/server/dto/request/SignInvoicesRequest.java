package com.einvoicehub.server.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

/**
 * Request ký số (phát hành) một danh sách hóa đơn.
 * Flow (API SignInvoices):
 *  - Giới hạn 30 hóa đơn mỗi lần gọi.
 *  - IDType xác định loại ID truyền vào invoiceIds:
 *      0 = dùng einv_invoices.id (UUID nội bộ HUB)
 *      1 = dùng einv_invoices.partner_invoice_id (ID từ POS)
 *  - Validate: hóa đơn phải tồn tại và chưa được ký (status_id ∈ {0,1,5,7}).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInvoicesRequest {

    @NotBlank(message = "Tenant ID is required")
    @Size(max = 36, message = "Tenant ID must not exceed 36 characters")
    @JsonProperty("tenant_id")
    private String tenantId;

    @NotBlank(message = "Store ID is required")
    @Size(max = 36, message = "Store ID must not exceed 36 characters")
    @JsonProperty("store_id")
    private String storeId;

    @Size(max = 36, message = "Provider ID must not exceed 36 characters")
    @JsonProperty("provider_id")
    private String providerId;

    /** Loại ID truyền vào invoiceIds:
     *   0 = einv_invoices.id  (UUID nội bộ HUB)
     *   1 = einv_invoices.partner_invoice_id  */
    @Min(value = 0, message = "ID type must be 0 or 1")
    @Max(value = 1, message = "ID type must be 0 or 1")
    @JsonProperty("id_type")
    @Builder.Default
    private Integer idType = 0;

    @NotNull(message = "Invoice IDs list is required")
    @NotEmpty(message = "Invoice IDs list must not be empty")
    @Size(max = 30, message = "Maximum 30 invoices per signing request")
    @JsonProperty("invoice_ids")
    private List<String> invoiceIds;

    /**Kiểu ký số: 0 = Token, 1 = HSM.
     * Nếu null, lấy sign_type từ einv_store_provider.
     */
    @Min(value = 0, message = "Sign type must be 0 (Token), 1 (HSM), or 2 (SmartCA)")
    @Max(value = 2, message = "Sign type must be 0 (Token), 1 (HSM), or 2 (SmartCA)")
    @JsonProperty("sign_type")
    private Byte signType;
}