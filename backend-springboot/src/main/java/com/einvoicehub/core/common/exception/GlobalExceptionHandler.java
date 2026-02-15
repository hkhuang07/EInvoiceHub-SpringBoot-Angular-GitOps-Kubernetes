package com.einvoicehub.core.common.exception;

import com.einvoicehub.core.provider.exception.ProviderException;
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

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex) {
        log.warn("Business Exception: [{}] - {}", ex.getErrorCode().getCode(), ex.getMessage());
        return buildResponse(ex.getErrorCode(), ex.getMessage(), null);
    }

    @ExceptionHandler(ProviderException.class)
    public ResponseEntity<ApiResponse<?>> handleProviderException(ProviderException ex) {
        log.error("Provider Error [{}]: {} - Status: {}", ex.getProviderCode(), ex.getMessage(), ex.getStatusCode());

        ApiResponse<Map<String, Object>> response = ApiResponse.<Map<String, Object>>builder()
                .code(ErrorCode.PROVIDER_ERROR)
                .message(ex.getMessage())
                .result(ex.getDetails())
                .build();
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        try {
            String firstMsg = ex.getBindingResult().getFieldError().getDefaultMessage();
            if (firstMsg != null) errorCode = ErrorCode.valueOf(firstMsg);
        } catch (Exception ignored) {}

        log.warn("Validation Failed: {}", errors);
        return buildResponse(errorCode, errorCode.getMessage(), errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {
        return buildResponse(ErrorCode.VALIDATION_ERROR, "Invalid parameter: " + ex.getMessage(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(ErrorCode.UNAUTHORIZED, "Access denied", null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidJson(HttpMessageNotReadableException ex) {
        return buildResponse(ErrorCode.VALIDATION_ERROR, "Invalid JSON format", null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = String.format("Parameter '%s' has an invalid data type", ex.getName());
        return buildResponse(ErrorCode.VALIDATION_ERROR, msg, null);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(NoHandlerFoundException ex) {
        return buildResponse(ErrorCode.VALIDATION_ERROR, "API endpoint not found: " + ex.getRequestURL(), null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return buildResponse(ErrorCode.VALIDATION_ERROR, "HTTP method not supported", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        log.error("CRITICAL SYSTEM ERROR: ", ex);
        return buildResponse(ErrorCode.UNCATEGORIZED_EXCEPTION, ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage(), null);
    }

    private ResponseEntity<ApiResponse<?>> buildResponse(ErrorCode errorCode, String message, Object result) {
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode)
                .message(message)
                .result(result)
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
}