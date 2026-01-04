package com.einvoicehub.core.provider;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Cấu hình kết nối Provider - Tối ưu bảo mật và đa dụng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderConfig {

    private Long configId;
    private String providerCode;
    private String providerName;

    private String username;

    @ToString.Exclude // KHÔNG in mật khẩu ra log
    private String password;

    /** Bổ sung cho các Provider dùng AppId/SecretKey (MISA, Viettel...) */
    private String appId;

    @ToString.Exclude // KHÔNG in secretKey ra log
    private String secretKey;

    private String customApiUrl;

    /** Quản lý chứng thư số (HSM hoặc Token) */
    private String certificateSerial;

    @ToString.Exclude
    private String certificateData;
    private LocalDateTime certificateExpiredAt;

    /** Cấu hình đặc thù cho từng hãng (dạng JSON) */
    private String extraConfigJson;

    @ToString.Exclude // KHÔNG in token ra log
    private String accessToken;
    private LocalDateTime tokenExpiredAt;

    public boolean hasValidCertificate() {
        return certificateData != null && !certificateData.isEmpty() &&
                (certificateExpiredAt == null || certificateExpiredAt.isAfter(LocalDateTime.now()));
    }

    /**
     * Kiểm tra Token với khoảng đệm 5 phút để đảm bảo giao dịch không bị ngắt quãng
     */
    public boolean hasValidToken() {
        if (accessToken == null || accessToken.isEmpty()) return false;
        if (tokenExpiredAt == null) return true;
        return tokenExpiredAt.isAfter(LocalDateTime.now().plusMinutes(5));
    }
}