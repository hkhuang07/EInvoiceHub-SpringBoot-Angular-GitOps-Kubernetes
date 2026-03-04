package com.einvoicehub.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Ngoại lệ tùy chỉnh để trả về mã lỗi 401 khi không xác định được danh tính khách hàng.
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TenantNotFoundException extends RuntimeException {
    public TenantNotFoundException(String message) {
        super(message);
    }
}