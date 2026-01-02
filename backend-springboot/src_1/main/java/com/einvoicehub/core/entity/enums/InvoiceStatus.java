package com.einvoicehub.core.entity.enums;

public enum InvoiceStatus {
    DRAFT("Bản nháp", "Hóa đơn đang được soạn thảo"),
    PENDING("Chờ xử lý", "Đã nhận yêu cầu, đang chờ xử lý"),
    SIGNING("Đang ký", "Đang thực hiện ký điện tử"),
    SENT_TO_PROVIDER("Đã gửi Provider", "Đã gửi sang nhà cung cấp"),
    SUCCESS("Thành công", "Hóa đơn đã phát hành thành công"),
    FAILED("Thất bại", "Có lỗi xảy ra trong quá trình xử lý"),
    CANCELLED("Đã hủy", "Hóa đơn đã bị hủy"),
    REPLACED("Đã thay thế", "Hóa đơn đã được thay thế bởi hóa đơn khác");

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

    public boolean isTerminal() {
        return this == SUCCESS || this == FAILED || this == CANCELLED || this == REPLACED;
    }

    public boolean isError() {
        return this == FAILED || this == CANCELLED;
    }
}