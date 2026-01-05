package com.einvoicehub.core.common.exception;

import com.einvoicehub.core.dto.response.ApiResponse;
import com.einvoicehub.core.provider.exception.ProviderException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global Exception Handler - "Trạm kiểm soát không lưu" đã hợp nhất của EInvoiceHub.
 * Đã sửa lỗi tương thích kiểu dữ liệu (ErrorCode thay vì int).
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. Xử lý lỗi từ các Nhà cung cấp hóa đơn (BKAV, MISA, VNPT, VIETTEL)
     */
    @ExceptionHandler(ProviderException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleProviderException(
            ProviderException ex, HttpServletRequest request) {

        log.error("Provider Error [{}]: {} - Status: {}",
                ex.getProviderCode(), ex.getMessage(), ex.getStatusCode());

        // FIX: Truyền thẳng đối tượng Enum ErrorCode vào builder
        ApiResponse<Map<String, Object>> response = ApiResponse.<Map<String, Object>>builder()
                .code(ErrorCode.PROVIDER_ERROR)
                .message(ex.getMessage())
                .result(ex.getDetails())
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    /**
     * 2. Xử lý AppException (Lỗi nghiệp vụ đã định nghĩa trong ErrorCode)
     */
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<?>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.warn("Business Exception: [{}] - {}", errorCode.getCode(), exception.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode)
                        .message(errorCode.getMessage())
                        .build());
    }

    /**
     * 3. Xử lý lỗi đặc thù: MerchantNotFound & InsufficientQuota
     */
    @ExceptionHandler({MerchantNotFoundException.class, InsufficientQuotaException.class})
    public ResponseEntity<ApiResponse<?>> handleDomainExceptions(RuntimeException ex) {
        log.warn("Domain Exception: {}", ex.getMessage());

        ErrorCode errorCode = (ex instanceof MerchantNotFoundException)
                ? ErrorCode.MERCHANT_NOT_FOUND
                : ErrorCode.INSUFFICIENT_QUOTA;

        // FIX: Truyền đối tượng Enum errorCode
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode)
                        .message(ex.getMessage())
                        .build());
    }

    /**
     * 4. Xử lý lỗi validation từ @Valid
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handlingValidation(
            MethodArgumentNotValidException exception) {

        Map<String, String> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value"
                ));

        log.warn("Validation Error: {}", validationErrors);

        String enumKey = exception.getBindingResult().getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;

        try {
            if (enumKey != null) errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            log.debug("Validation message '{}' không phải là ErrorCode key", enumKey);
        }

        // FIX: Truyền đối tượng Enum errorCode
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.<Map<String, String>>builder()
                        .code(errorCode)
                        .message(errorCode.getMessage())
                        .result(validationErrors)
                        .build());
    }

    /**
     * 5. Xử lý lỗi định dạng dữ liệu & Endpoint
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handlingHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        // FIX: Truyền đối tượng Enum ErrorCode
        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatusCode())
                .body(ApiResponse.builder()
                        .code(ErrorCode.VALIDATION_ERROR)
                        .message("Định dạng JSON không hợp lệ: " + extractRootCause(exception))
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handlingTypeMismatch(MethodArgumentTypeMismatchException exception) {
        String msg = String.format("Tham số '%s' nhận giá trị '%s' không đúng kiểu dữ liệu mong đợi",
                exception.getName(), exception.getValue());

        // FIX: Truyền đối tượng Enum ErrorCode
        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatusCode())
                .body(ApiResponse.builder()
                        .code(ErrorCode.VALIDATION_ERROR)
                        .message(msg)
                        .build());
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handlingNotFound(NoHandlerFoundException exception) {
        // FIX: Truyền đối tượng Enum ErrorCode
        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatusCode())
                .body(ApiResponse.builder()
                        .code(ErrorCode.VALIDATION_ERROR)
                        .message("API không tồn tại: " + exception.getRequestURL())
                        .build());
    }

    /**
     * 6. Fallback - Lưới an toàn cuối cùng cho mọi Exception
     */
    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    public ResponseEntity<ApiResponse<?>> handlingGenericException(Exception exception) {
        log.error("Critical System Error: ", exception);

        // FIX: Truyền đối tượng Enum ErrorCode
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode())
                .body(ApiResponse.builder()
                        .code(ErrorCode.UNCATEGORIZED_EXCEPTION)
                        .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                        .build());
    }

    private String extractRootCause(Exception e) {
        return (e.getCause() != null && e.getCause().getMessage() != null)
                ? e.getCause().getMessage() : "Dữ liệu không đúng cấu trúc";
    }
}