package com.einvoicehub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EinvMerchantUserRequest {

    @NotNull(message = "Merchant ID là bắt buộc")
    @JsonProperty("MerchantID")
    private Long merchantId;

    @NotBlank(message = "Username không được để trống")
    @Size(min = 4, max = 50)
    @JsonProperty("Username")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email
    @JsonProperty("Email")
    private String email;

    /**
     * Trong Request gửi lên là mật khẩu thô (Plain text),
     * sau đó Service sẽ mã hóa thành password_hash để lưu.
     */
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu tối thiểu 8 ký tự")
    @JsonProperty("Password")
    private String password;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("Role")
    private String role; // ADMIN, MANAGER, STAFF,VIEWER

    @JsonProperty("IsPrimary")
    private Boolean isPrimary;
}