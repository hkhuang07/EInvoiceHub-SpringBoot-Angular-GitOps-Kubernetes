package com.einvoicehub.core.domain.entity.enums;

public enum SyncType {
    SEND_TO_PROVIDER,   // Gửi hóa đơn sang nhà cung cấp dịch vụ
    SEND_TO_CQT,        // Gửi thông tin sang Cơ quan Thuế
    CANCEL_INVOICE,     // Hủy hóa đơn trên hệ thống Provider
    REPLACE_INVOICE     // Thay thế hóa đơn trên hệ thống Provider
}