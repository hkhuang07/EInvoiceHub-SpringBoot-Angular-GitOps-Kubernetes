package com.einvoicehub.core.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception khi vượt hạn mức hóa đơn.
 */
@Getter
public class InsufficientQuotaException extends RuntimeException {
    private final String errorCode = "QUOTA_EXCEEDED";
    private final HttpStatus status = HttpStatus.FORBIDDEN;

    public InsufficientQuotaException(String message) {
        super(message);
    }
}