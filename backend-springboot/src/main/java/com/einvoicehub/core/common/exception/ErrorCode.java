package com.einvoicehub.core.common.exception;

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
    SUCCESS(1000, "Success", HttpStatus.OK),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized system error", HttpStatus.INTERNAL_SERVER_ERROR),
    MERCHANT_NOT_FOUND(1001, "Merchant not found", HttpStatus.NOT_FOUND),
    INSUFFICIENT_QUOTA(1002, "Merchant has exceeded invoice quota", HttpStatus.FORBIDDEN),
    VALIDATION_ERROR(1003, "Invalid input data", HttpStatus.BAD_REQUEST),
    PROVIDER_ERROR(1004, "Service provider error", HttpStatus.BAD_GATEWAY),
    API_KEY_INVALID(1005, "Invalid or expired API Key", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "Access denied", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(401, "Authentication failed", HttpStatus.UNAUTHORIZED);

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