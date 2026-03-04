package com.einvoicehub.core.exception;

import lombok.Getter;

@Getter
public class InvalidDataException extends AppException {

    public InvalidDataException(String message) {
        super(ErrorCode.INVALID_DATA, message);
    }

    public InvalidDataException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidDataException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}