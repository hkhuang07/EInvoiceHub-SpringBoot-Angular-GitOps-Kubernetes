package com.einvoicehub.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception khi không tìm thấy Merchant.
 */
@Getter
public class MerchantNotFoundException extends AppException {
    private final String errorCode = "MERCHANT_NOT_FOUND";
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public MerchantNotFoundException(String merchantCode) {
        super(ErrorCode.MERCHANT_NOT_FOUND);     }
}