package com.einvoicehub.core.common.exception;

/**
 * Exception được ném ra khi không tìm thấy merchant trong hệ thống.
 * Đây là một unchecked exception kế thừa từ RuntimeException.
 */
public class MerchantNotFoundException extends RuntimeException {

    /**
     * Constructor nhận vào mã merchant không tìm thấy.
     *
     * @param merchantCode mã của merchant không tìm thấy trong hệ thống
     */
    public MerchantNotFoundException(String merchantCode) {
        super("Không tìm thấy merchant với mã: " + merchantCode);
    }

    /**
     * Constructor nhận vào thông báo lỗi tùy chỉnh.
     *
     * @param message thông báo lỗi mô tả chi tiết
     */
    public MerchantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
