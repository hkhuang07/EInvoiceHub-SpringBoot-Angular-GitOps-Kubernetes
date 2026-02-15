package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvInvoiceTemplateRequest {

    @NotNull(message = "Merchant ID là bắt buộc")
    @JsonProperty("MerchantID")
    private Long merchantId;

    @NotNull(message = "Loại hóa đơn ID là bắt buộc")
    @JsonProperty("InvoiceTypeID")
    private Long invoiceTypeId;

    @JsonProperty("RegistrationID")
    private Long registrationId;

    @NotBlank(message = "Mẫu hóa đơn không được để trống")
    @Size(max = 20)
    @JsonProperty("TemplateCode")
    private String templateCode;

    @NotBlank(message = "Ký hiệu hóa đơn không được để trống")
    @Size(max = 10)
    @JsonProperty("SymbolCode")
    private String symbolCode;

    @JsonProperty("MinNumber")
    private Integer minNumber;

    @JsonProperty("MaxNumber")
    private Integer maxNumber;

    @JsonProperty("StartDate")
    private LocalDateTime startDate;

    @JsonProperty("IsActive")
    private Boolean isActive;

    @JsonProperty("ProviderID")
    private String providerId;

    @JsonProperty("ProviderSerialID")
    private String providerSerialId;
}