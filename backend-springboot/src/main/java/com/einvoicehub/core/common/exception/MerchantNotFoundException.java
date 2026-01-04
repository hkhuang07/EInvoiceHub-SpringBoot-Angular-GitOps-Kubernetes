package com.einvoicehub.core.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception khi không tìm thấy Merchant.
 */
@Getter
public class MerchantNotFoundException extends RuntimeException {
    private final String errorCode = "MERCHANT_NOT_FOUND";
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public MerchantNotFoundException(String merchantCode) {
        super("Không tìm thấy merchant với mã: " + merchantCode);
    }
}