package com.einvoicehub.core.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler cho toàn bộ ứng dụng EInvoiceHub.
 * Xử lý các exception tùy chỉnh và trả về response nhất quán cho client.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Xử lý exception khi merchant không tìm thấy trong hệ thống.
     *
     * @param ex exception MerchantNotFoundException
     * @return response lỗi với HTTP status NOT_FOUND
     */
    @ExceptionHandler(MerchantNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMerchantNotFoundException(MerchantNotFoundException ex) {
        log.warn("Merchant không tìm thấy: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Xử lý exception khi merchant vượt quá hạn mức hóa đơn.
     *
     * @param ex exception InsufficientQuotaException
     * @return response lỗi với HTTP status FORBIDDEN
     */
    @ExceptionHandler(InsufficientQuotaException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientQuotaException(InsufficientQuotaException ex) {
        log.warn("Vượt quá hạn mức hóa đơn: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    /**
     * Xử lý các exception không mong đợi khác.
     *
     * @param ex exception không mong đợi
     * @return response lỗi với HTTP status INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Lỗi không mong đợi xảy ra: ", ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Đã xảy ra lỗi nội bộ. Vui lòng thử lại sau."
        );
    }

    /**
     * Phương thức helper để xây dựng response lỗi chuẩn hóa.
     *
     * @param status HTTP status code
     * @param message thông báo lỗi
     * @return ResponseEntity chứa ErrorResponse
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Data class đại diện cho cấu trúc response lỗi.
     */
    @Data
    @Builder
    @AllArgsConstructor
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
    }
}
