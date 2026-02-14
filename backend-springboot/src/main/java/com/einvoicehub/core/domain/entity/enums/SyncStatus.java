package com.einvoicehub.core.domain.entity.enums;

public enum SyncStatus {
    PENDING,    // Chờ xử lý
    PROCESSING, // Đang xử lý
    COMPLETED,  // Đã hoàn thành thành công
    FAILED,     // Thất bại sau khi đã thử đủ số lần
    RETRYING    // Đang chờ để thử lại
}