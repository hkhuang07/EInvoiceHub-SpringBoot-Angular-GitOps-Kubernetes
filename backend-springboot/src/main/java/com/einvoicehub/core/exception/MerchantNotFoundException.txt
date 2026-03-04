package com.einvoicehub.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MerchantNotFoundException extends AppException {
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public MerchantNotFoundException(String merchantCode) {
        super(ErrorCode.MERCHANT_NOT_FOUND, "Không tìm thấy Merchant với mã: " + merchantCode);
    }
}