package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvMerchantProviderConfigRequest {

    @NotNull(message = "Merchant ID là bắt buộc")
    @JsonProperty("MerchantID")
    private Long merchantId;

    @NotNull(message = "Provider ID là bắt buộc")
    @JsonProperty("ProviderID")
    private Long providerId;

    @JsonProperty("PartnerID")
    private String partnerId;

    @JsonProperty("PartnerToken")
    private String partnerToken;

    @NotBlank(message = "Mã số thuế kết nối không được để trống")
    @Size(max = 20)
    @JsonProperty("TaxCode")
    private String taxCode;

    @JsonProperty("IntegrationUrl")
    private String integrationUrl;

    @NotBlank(message = "Tài khoản dịch vụ (Username) không được để trống")
    @JsonProperty("UsernameService")
    private String usernameService;

    @NotBlank(message = "Mật khẩu dịch vụ không được để trống")
    @JsonProperty("PasswordService")
    private String passwordService; // Mật khẩu thô để Service xử lý mã hóa

    @JsonProperty("IsTestMode")
    private Boolean isTestMode;

    @JsonProperty("IsActive")
    private Boolean isActive;

    @JsonProperty("IsDefault")
    private Boolean isDefault;

    @JsonProperty("ExtraConfig")
    private String extraConfig; // Dữ liệu JSON để config
}