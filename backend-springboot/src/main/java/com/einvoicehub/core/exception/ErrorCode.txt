package com.einvoicehub.core.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ErrorCode {
    SUCCESS(1000, "Thành công", HttpStatus.OK),
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống không xác định", HttpStatus.INTERNAL_SERVER_ERROR),

    // Auth & Access
    UNAUTHORIZED(403, "Không có quyền truy cập", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(401, "Chưa xác thực tài khoản", HttpStatus.UNAUTHORIZED),
    API_KEY_INVALID(1005, "API Key không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED),

    // Merchant Errors
    MERCHANT_NOT_FOUND(1001, "Không tìm thấy doanh nghiệp", HttpStatus.NOT_FOUND),
    INSUFFICIENT_QUOTA(1002, "Doanh nghiệp đã hết hạn mức hóa đơn", HttpStatus.FORBIDDEN),

    // Validation & Data Errors
    VALIDATION_ERROR(1003, "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_DATA(1006, "Dữ liệu nghiệp vụ không hợp lệ", HttpStatus.BAD_REQUEST), // Thêm cho InvalidDataException

    // Invoice Business Errors
    TEMPLATE_NOT_FOUND(2001, "Không tìm thấy mẫu hóa đơn", HttpStatus.NOT_FOUND),
    REGISTRATION_EXPIRED(2002, "Dải số hóa đơn đã hết hạn", HttpStatus.BAD_REQUEST),
    REGISTRATION_EXHAUSTED(2003, "Dải số hóa đơn đã dùng hết", HttpStatus.BAD_REQUEST),

    PROVIDER_ERROR(1004, "Lỗi kết nối từ nhà cung cấp dịch vụ (Provider)", HttpStatus.BAD_GATEWAY);

    private static final Map<Integer, ErrorCode> CACHE = Stream.of(values())
            .collect(Collectors.toMap(ErrorCode::getCode, Function.identity()));

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
        return Optional.ofNullable(CACHE.get(code));
    }
}