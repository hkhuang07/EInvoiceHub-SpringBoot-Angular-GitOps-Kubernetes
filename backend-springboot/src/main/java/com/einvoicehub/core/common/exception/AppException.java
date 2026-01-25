package com.einvoicehub.core.common.exception;

import lombok.Getter;
import java.util.Collections;
import java.util.Map;

@Getter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public AppException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), Collections.emptyMap());
    }

    public AppException(ErrorCode errorCode, String customMessage) {
        this(errorCode, customMessage, Collections.emptyMap());
    }

    public AppException(ErrorCode errorCode, Map<String, Object> details) {
        this(errorCode, errorCode.getMessage(), details);
    }

    public AppException(ErrorCode errorCode, String customMessage, Map<String, Object> details) {
        super(customMessage);
        this.errorCode = errorCode;
        this.details = details != null ? details : Collections.emptyMap();
    }

    /* Exception chaining support */
    public AppException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.details = Collections.emptyMap();
    }

    @Override
    public String toString() {
        return String.format("AppException[errorCode=%s, code=%d, message='%s']",
                errorCode.name(), errorCode.getCode(), getMessage());
    }
}