package com.einvoicehub.core.exception;

import com.einvoicehub.core.dto.EinvoiceHubResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Xử lý lỗi nghiệp vụ */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<EinvoiceHubResponse<?>> handleAppException(AppException ex) {
        log.warn("Business Logic Exception: [{}] - {}", ex.getErrorCode().getCode(), ex.getMessage());
        return buildResponse(ex.getErrorCode(), ex.getMessage(), ex.getDetails());
    }

    /** lỗi trả về từ Nhà cung cấp HĐĐT (BKAV, VNPT, MISA...) */
    @ExceptionHandler(ProviderException.class)
    public ResponseEntity<EinvoiceHubResponse<?>> handleProviderException(ProviderException ex) {
        log.error("Provider Integration Error [{}]: {}", ex.getProviderCode(), ex.getMessage());

        EinvoiceHubResponse<Object> response = EinvoiceHubResponse.builder()
                .responseCode(String.valueOf(ErrorCode.PROVIDER_ERROR.getCode()))
                .responseDesc("Lỗi từ nhà cung cấp: " + ex.getMessage())
                .data(ex.getDetails())
                .build();
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    /** Xử lý lỗi Validation dữ liệu đầu vào */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EinvoiceHubResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        log.warn("Validation Failed: {}", errors);
        return buildResponse(ErrorCode.VALIDATION_ERROR, "Dữ liệu đầu vào không hợp lệ", errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<EinvoiceHubResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {
        return buildResponse(ErrorCode.VALIDATION_ERROR, "Tham số không hợp lệ: " + ex.getMessage(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<EinvoiceHubResponse<?>> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(ErrorCode.UNAUTHORIZED, "Bạn không có quyền truy cập tài nguyên này", null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<EinvoiceHubResponse<?>> handleInvalidJson(HttpMessageNotReadableException ex) {
        return buildResponse(ErrorCode.VALIDATION_ERROR, "Định dạng JSON không hợp lệ", null);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<EinvoiceHubResponse<?>> handleNotFound(NoHandlerFoundException ex) {
        return buildResponse(ErrorCode.VALIDATION_ERROR, "Không tìm thấy API: " + ex.getRequestURL(), null);
    }

    /** Xử lý các lỗi hệ thống chưa được định nghĩa (Crash, NullPointer...) */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<EinvoiceHubResponse<?>> handleGenericException(Exception ex) {
        log.error("CRITICAL SYSTEM ERROR: ", ex);
        return buildResponse(ErrorCode.UNCATEGORIZED_EXCEPTION, "Lỗi hệ thống ngoài ý muốn", null);
    }

    /** Helper build response theo chuẩn PascalCase của Softz */
    private ResponseEntity<EinvoiceHubResponse<?>> buildResponse(ErrorCode errorCode, String message, Object result) {
        EinvoiceHubResponse<?> response = EinvoiceHubResponse.builder()
                .responseCode(String.valueOf(errorCode.getCode()))
                .responseDesc(message)
                .data(result)
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
}