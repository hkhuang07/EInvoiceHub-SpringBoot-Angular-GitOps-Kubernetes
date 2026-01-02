package com.einvoicehub.entity;

/**
 * Invoice Status Enum
 * 
 * Định nghĩa các trạng thái của hóa đơn trong hệ thống
 */
public enum InvoiceStatus {
    DRAFT("Nháp", "Hóa đơn đang được tạo, chưa gửi đi"),
    PENDING("Đang chờ", "Hóa đơn đã gửi, đang chờ xử lý"),
    PROCESSING("Đang xử lý", "Provider đang xử lý hóa đơn"),
    ISSUED("Đã phát hành", "Hóa đơn đã được phát hành thành công"),
    SIGNED("Đã ký số", "Hóa đơn đã được ký số"),
    CANCELLED("Đã hủy", "Hóa đơn đã bị hủy"),
    REPLACED("Đã thay thế", "Hóa đơn đã được thay thế bởi hóa đơn khác"),
    ADJUSTED("Đã điều chỉnh", "Hóa đơn đã được điều chỉnh"),
    FAILED("Thất bại", "Có lỗi xảy ra trong quá trình xử lý"),
    ERROR("Lỗi", "Hóa đơn gặp lỗi");

    private final String displayName;
    private final String description;

    InvoiceStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Kiểm tra trạng thái có phải là trạng thái kết thúc không
     */
    public boolean isTerminal() {
        return this == ISSUED || 
               this == SIGNED || 
               this == CANCELLED || 
               this == REPLACED || 
               this == ADJUSTED || 
               this == FAILED ||
               this == ERROR;
    }

    /**
     * Kiểm tra trạng thái có phải là trạng thái thành công không
     */
    public boolean isSuccess() {
        return this == ISSUED || this == SIGNED;
    }

    /**
     * Kiểm tra trạng thái có thể hủy không
     */
    public boolean isCancellable() {
        return this == DRAFT || 
               this == PENDING || 
               this == PROCESSING || 
               this == ISSUED;
    }

    /**
     * Kiểm tra trạng thái có thể điều chỉnh không
     */
    public boolean isAdjustable() {
        return this == ISSUED || this == SIGNED;
    }
}