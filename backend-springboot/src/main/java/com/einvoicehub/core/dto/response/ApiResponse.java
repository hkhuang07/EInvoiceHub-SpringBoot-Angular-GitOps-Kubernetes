package com.einvoicehub.core.dto.response;

import com.einvoicehub.core.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO chuẩn hóa cho response của tất cả API endpoints - Bản Professional Type-Safe.
 * Sử dụng ErrorCode Enum để quản lý mã kết quả thay vì kiểu int thuần túy.
 *
 * @param <T> kiểu dữ liệu của result
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Mã kết quả dưới dạng Enum ErrorCode.
     * Khi trả về JSON, Jackson sẽ tự động chuyển thành số (1000, 1001...)
     * nhờ vào annotation @JsonValue đã đặt trong file ErrorCode.
     */
    private ErrorCode code;

    /**
     * Thông báo mô tả kết quả.
     */
    private String message;

    /**
     * Dữ liệu trả về (Generic Type).
     */
    private T result;

    /**
     * Thời điểm response được tạo (ISO 8601 format).
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Tạo response thành công không có dữ liệu.
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .code(ErrorCode.SUCCESS) // Sử dụng Enum thay vì số 1000
                .message(ErrorCode.SUCCESS.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Tạo response thành công với dữ liệu.
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(ErrorCode.SUCCESS)
                .message(ErrorCode.SUCCESS.getMessage())
                .result(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Tạo response thành công với dữ liệu và message tùy chỉnh.
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .code(ErrorCode.SUCCESS)
                .message(message)
                .result(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Tạo response lỗi trực tiếp từ ErrorCode Enum.
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .code(errorCode)
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Tạo response lỗi với ErrorCode và message tùy chỉnh (nếu cần ghi đè message mặc định).
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode, String customMessage) {
        return ApiResponse.<T>builder()
                .code(errorCode)
                .message(customMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Kiểm tra response có thành công không.
     * So sánh trực tiếp với Enum Object - Cực kỳ an toàn và chuyên nghiệp.
     */
    public boolean isSuccess() {
        return this.code == ErrorCode.SUCCESS;
    }

    /**
     * Kiểm tra response có phải lỗi không.
     */
    public boolean isError() {
        return this.code != ErrorCode.SUCCESS;
    }
}