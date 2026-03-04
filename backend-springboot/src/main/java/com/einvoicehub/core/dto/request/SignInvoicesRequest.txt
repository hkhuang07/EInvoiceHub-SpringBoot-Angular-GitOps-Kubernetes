package com.einvoicehub.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInvoicesRequest {

    /**  Loại ID gửi lên:
         0: InvoiceID (ID tự tăng của hệ thống HUB)
         1: PartnerInvoiceID (ID tham chiếu từ hệ thống POS - String)
         Mặc định = 0 */
    @Builder.Default
    @JsonProperty("IDType")
    private Integer idType = 0;

    @NotBlank(message = "InvoiceID không được để trống")
    @JsonProperty("InvoiceID")
    private String invoiceId;

    /** Loại hình ký số (ghi đè cấu hình nếu cần):
        0: Token, 1: HSM, 2: SmartCA */
    @JsonProperty("SignType")
    private Integer signType;
}