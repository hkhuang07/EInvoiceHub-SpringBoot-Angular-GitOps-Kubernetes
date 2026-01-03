package com.einvoicehub.core.common.exception;

/**
 * Exception được ném ra khi merchant vượt quá hạn mức hóa đơn được phép tạo.
 * Đây là một unchecked exception kế thừa từ RuntimeException.
 */
public class InsufficientQuotaException extends RuntimeException {

    /**
     * Constructor nhận vào thông báo lỗi mô tả chi tiết về việc vượt hạn mức.
     *
     * @param message thông báo lỗi mô tả chi tiết về việc vượt hạn mức
     */
    public InsufficientQuotaException(String message) {
        super(message);
    }

    /**
     * Constructor nhận vào thông báo lỗi và nguyên nhân gốc của exception.
     *
     * @param message thông báo lỗi mô tả chi tiết về việc vượt hạn mức
     * @param cause exception gốc gây ra lỗi này
     */
    public InsufficientQuotaException(String message, Throwable cause) {
        super(message, cause);
    }
}
