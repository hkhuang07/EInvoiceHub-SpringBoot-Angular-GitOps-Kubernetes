package com.einvoicehub.core.exception;

import lombok.Getter;


@Getter
public class InsufficientQuotaException extends AppException {
    public InsufficientQuotaException(String message) {
        super(ErrorCode.INSUFFICIENT_QUOTA);
    }

}