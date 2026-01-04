package com.einvoicehub.core.provider.exception;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Exception tùy chỉnh cho Provider - Tối ưu cho việc điều phối và xử lý lỗi tập trung.
 * Hỗ trợ cơ chế Retry và mang theo dữ liệu lỗi chi tiết từ đối tác.
 */
@Getter
public class ProviderException extends RuntimeException {

    private final String providerCode;
    private final String errorCode;
    private final int statusCode; // Mã trạng thái HTTP (400, 401, 500...)
    private final boolean retryable;
    private final LocalDateTime timestamp;
    private final Map<String, Object> details; // Chứa dữ liệu lỗi chi tiết từ Provider

    public ProviderException(String message, String providerCode, String errorCode) {
        this(message, providerCode, errorCode, 500, false, null, null);
    }

    public ProviderException(String message, String providerCode, String errorCode, int statusCode) {
        this(message, providerCode, errorCode, statusCode, false, null, null);
    }

    public ProviderException(String message, String providerCode, String errorCode,
                             int statusCode, boolean retryable) {
        this(message, providerCode, errorCode, statusCode, retryable, null, null);
    }

    /**
     * Constructor đầy đủ nhất để phục vụ việc truyền tải ngữ cảnh lỗi.
     */
    public ProviderException(String message, String providerCode, String errorCode,
                             int statusCode, boolean retryable,
                             Map<String, Object> details, Throwable cause) {
        super(message, cause);
        this.providerCode = providerCode;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.retryable = retryable;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}