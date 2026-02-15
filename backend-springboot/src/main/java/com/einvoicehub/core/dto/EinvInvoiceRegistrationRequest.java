package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceRegistrationRequest {

    @NotNull(message = "Merchant ID là bắt buộc")
    @JsonProperty("MerchantID")
    private Long merchantId;

    @NotBlank(message = "Số quyết định/phê duyệt không được để trống")
    @Size(max = 50)
    @JsonProperty("RegistrationNumber")
    private String registrationNumber;

    @NotNull(message = "Số bắt đầu là bắt buộc")
    @Min(value = 1)
    @JsonProperty("FromNumber")
    private Long fromNumber;

    @NotNull(message = "Số kết thúc là bắt buộc")
    @Min(value = 1)
    @JsonProperty("ToNumber")
    private Long toNumber;

    @NotNull(message = "Số lượng hóa đơn là bắt buộc")
    @Positive
    @JsonProperty("Quantity")
    private Long quantity;

    @NotNull(message = "Ngày hiệu lực là bắt buộc")
    @JsonProperty("EffectiveDate")
    private LocalDate effectiveDate;

    @JsonProperty("ExpirationDate")
    private LocalDate expirationDate;

    @NotNull(message = "ID trạng thái là bắt buộc")
    @JsonProperty("StatusID")
    private Integer statusId;

    @JsonProperty("IssuedBy")
    private String issuedBy;

    @JsonProperty("Note")
    private String note;
}