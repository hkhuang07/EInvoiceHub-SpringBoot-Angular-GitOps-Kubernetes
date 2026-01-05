package com.einvoicehub.core.common.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ErrorCode {
    SUCCESS(1000, "Thành công", HttpStatus.OK),
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    MERCHANT_NOT_FOUND(1001, "Không tìm thấy Merchant", HttpStatus.NOT_FOUND),
    INSUFFICIENT_QUOTA(1002, "Merchant đã vượt quá hạn mức hóa đơn", HttpStatus.FORBIDDEN),
    VALIDATION_ERROR(1003, "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    PROVIDER_ERROR(1004, "Lỗi từ nhà cung cấp hóa đơn", HttpStatus.BAD_GATEWAY),
    API_KEY_INVALID(1005, "API Key không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED);

    /**
     * @JsonValue: Giúp trả về số (1000, 1001...) khi serialize ra JSON
     * nhưng trong code Java vẫn dùng kiểu ErrorCode.
     */
    @JsonValue
    private final int code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public static Optional<ErrorCode> findByCode(int code) {
        return Arrays.stream(ErrorCode.values())
                .filter(errorCode -> errorCode.code == code)
                .findFirst();
    }
}