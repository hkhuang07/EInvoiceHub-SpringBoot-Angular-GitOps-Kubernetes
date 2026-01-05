package com.einvoicehub.core.provider.exception;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Getter
public class ProviderException extends RuntimeException {
    private final String providerCode;
    private final String errorCode;
    private final int statusCode;
    private final boolean retryable;
    private final LocalDateTime timestamp;
    private final Map<String, Object> details;

    public ProviderException(String message, String providerCode, String errorCode) {
        this(message, providerCode, errorCode, 500, false, null, null);
    }

    public ProviderException(String message, String providerCode, String errorCode, int statusCode) {
        this(message, providerCode, errorCode, statusCode, false, null, null);
    }

    public ProviderException(String message, String providerCode, String errorCode,
                             int statusCode, boolean retryable,
                             Map<String, Object> details, Throwable cause) {
        super(message, cause);
        this.providerCode = providerCode;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.retryable = retryable;
        this.details = details != null ? details : Collections.emptyMap();
        this.timestamp = LocalDateTime.now();
    }
}