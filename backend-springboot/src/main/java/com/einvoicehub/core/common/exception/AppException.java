package com.einvoicehub.core.common.exception;

import lombok.Getter;
import java.util.Map;

/**
 * Custom exception chính của ứng dụng EInvoiceHub.
 * Được thiết kế để bọc các lỗi nghiệp vụ (Business Logic) và đảm bảo
 * tính nhất quán về kiểu dữ liệu ErrorCode trong toàn bộ hệ thống.
 */
@Getter
public class AppException extends RuntimeException {

    /**
     * Enum ErrorCode chứa thông tin chi tiết về mã lỗi và HTTP Status.
     */
    private final ErrorCode errorCode;

    /**
     * Dữ liệu bổ sung đi kèm lỗi (Ví dụ: danh sách các trường validation sai).
     * Giúp ApiResponse trả về thông tin chi tiết cho Client.
     */
    private final Map<String, Object> details;

    /**
     * Constructor cơ bản từ ErrorCode.
     */
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    /**
     * Constructor với message tùy chỉnh và dữ liệu chi tiết.
     */
    public AppException(ErrorCode errorCode, String customMessage, Map<String, Object> details) {
        super(customMessage);
        this.errorCode = errorCode;
        this.details = details;
    }

    /**
     * Constructor bọc exception gốc (Chain Exception).
     */
    public AppException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.details = null;
    }

    /**
     * FIX LỖI: Thay đổi kiểu trả về từ int sang ErrorCode.
     * Điều này giúp GlobalExceptionHandler gán trực tiếp vào đối tượng Enum.
     * * @return Đối tượng Enum ErrorCode (Không phải mã số int)
     */
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    /**
     * Lấy mã số lỗi (int) khi cần thiết (Ví dụ: cho Logging).
     */
    public int getRawErrorCode() {
        return errorCode.getCode();
    }

    /**
     * Lấy HTTP status code tương ứng.
     */
    public int getHttpStatusCode() {
        return errorCode.getStatusCode().value();
    }

    @Override
    public String toString() {
        return String.format("AppException{errorCode=%s, code=%d, message='%s', details=%s}",
                errorCode.name(), errorCode.getCode(), getMessage(), details);
    }
}