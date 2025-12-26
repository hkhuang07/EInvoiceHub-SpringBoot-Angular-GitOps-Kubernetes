package com.einvoicehub.core.provider;

import lombok.*;
import java.time.LocalDateTime;

/**Cấu hình kết nối Provider cho Merchant*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderConfig {

    /**ID cấu hình*/
    private Long configId;

    /**Mã Provider*/
    private String providerCode;

    /**Tên Provider*/
    private String providerName;

    /**Tài khoản API*/
    private String username;

    /**Mật khẩu API (đã giải mã)*/
    private String password;

    /**URL API tùy chỉnh (nếu có)*/
    private String customApiUrl;

    /**Serial chứng thư số*/
    private String certificateSerial;

    /**Dữ liệu chứng thư số (đã giải mã)*/
    private String certificateData;

    /**Hạn chứng thư số*/
    private LocalDateTime certificateExpiredAt;

    /**Cấu hình bổ sung (JSON)*/
    private String extraConfigJson;

    /**Token xác thực (nếu Provider dùng OAuth)*/
    private String accessToken;

    /**Thời điểm hết hạn token*/
    private LocalDateTime tokenExpiredAt;

    public boolean hasValidCertificate() {
        return certificateData != null && !certificateData.isEmpty() &&
                (certificateExpiredAt == null || certificateExpiredAt.isAfter(LocalDateTime.now()));
    }

    public boolean hasValidToken() {
        return accessToken != null && !accessToken.isEmpty() &&
                (tokenExpiredAt == null || tokenExpiredAt.isAfter(LocalDateTime.now()));
    }
}