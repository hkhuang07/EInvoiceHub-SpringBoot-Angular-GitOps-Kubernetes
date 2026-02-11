package com.einvoicehub.core.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class InsufficientQuotaException extends AppException {
    public InsufficientQuotaException(String message) {
        super(ErrorCode.INSUFFICIENT_QUOTA);
    }

}