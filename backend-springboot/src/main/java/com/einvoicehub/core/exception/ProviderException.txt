package com.einvoicehub.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**Exception đặc thù cho các lỗi phát sinh từ Nhà cung cấp HĐĐT (BKAV, VNPT, MISA...).*/
@Getter
public class ProviderException extends AppException {

    private final String providerCode; // VD: BKAV, VNPT, MOBI
    private final int statusCode;      // HTTP Status Code từ đối tác trả về

    public ProviderException(String providerCode, String message) {
        super(ErrorCode.PROVIDER_ERROR, message);
        this.providerCode = providerCode;
        this.statusCode = ErrorCode.PROVIDER_ERROR.getStatusCode().value();
    }

    public ProviderException(String providerCode, int statusCode, String message, Map<String, Object> details) {
        super(ErrorCode.PROVIDER_ERROR, message, details);
        this.providerCode = providerCode;
        this.statusCode = statusCode;
    }

    public ProviderException(String providerCode, String message, Throwable cause) {
        super(ErrorCode.PROVIDER_ERROR, cause);
        this.providerCode = providerCode;
        this.statusCode = HttpStatus.BAD_GATEWAY.value();
    }

    @Override
    public String toString() {
        return String.format("ProviderException[provider=%s, status=%d, message='%s']",
                providerCode, statusCode, getMessage());
    }
}