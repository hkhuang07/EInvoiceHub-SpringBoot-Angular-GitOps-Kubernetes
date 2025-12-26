package com.einvoicehub.core.provider;

import lombok.Getter;

/**Exception tùy chỉnh cho Provider*/
@Getter
public class ProviderException extends RuntimeException {

    private final String providerCode;
    private final String errorCode;
    private final boolean retryable;

    public ProviderException(String message, String providerCode, String errorCode) {
        super(message);
        this.providerCode = providerCode;
        this.errorCode = errorCode;
        this.retryable = false;
    }

    public ProviderException(String message, String providerCode, String errorCode, boolean retryable) {
        super(message);
        this.providerCode = providerCode;
        this.errorCode = errorCode;
        this.retryable = retryable;
    }

    public ProviderException(String message, String providerCode, String errorCode, Throwable cause) {
        super(message, cause);
        this.providerCode = providerCode;
        this.errorCode = errorCode;
        this.retryable = false;
    }

    public ProviderException(String message, String providerCode, String errorCode,
                             boolean retryable, Throwable cause) {
        super(message, cause);
        this.providerCode = providerCode;
        this.errorCode = errorCode;
        this.retryable = retryable;
    }
}