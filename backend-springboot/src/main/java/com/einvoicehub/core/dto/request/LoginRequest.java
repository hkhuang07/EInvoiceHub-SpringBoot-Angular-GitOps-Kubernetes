package com.einvoicehub.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @JsonProperty("Username")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @JsonProperty("Password")
    private String password;
}