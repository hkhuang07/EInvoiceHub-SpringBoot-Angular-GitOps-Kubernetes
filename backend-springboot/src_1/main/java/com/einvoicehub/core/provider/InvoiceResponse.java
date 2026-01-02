package com.einvoicehub.core.provider;

import lombok.*;
import java.time.LocalDateTime;

/**DTO phản hồi từ Provider sau khi phát hành hóa đơn*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {

    /**Trạng thái xử lý*/
    private ResponseStatus status;

    /** Mã lỗi từ Provider (nếu có)*/
    private String errorCode;

    /**Thông báo lỗi chi tiết*/
    private String errorMessage;

    /**Mã giao dịch từ Provider*/
    private String transactionCode;

    /**Số hóa đơn được cấp*/
    private String invoiceNumber;

    /**Ký hiệu hóa đơn*/
    private String symbolCode;

    /**Mẫu hóa đơn*/
    private String templateCode;

    /**Ngày phát hành*/
    private String issueDate;

    /**Mã tra cứu điện tử*/
    private String lookupCode;

    /**Mã xác thực*/
    private String securityCode;

    /**URL file PDF hóa đơn*/
    private String pdfUrl;

    /**Dữ liệu XML gốc*/
    private String xmlData;

    /**Thời điểm phản hồi*/
    private LocalDateTime responseTime;

    /**Dữ liệu thô từ Provider*/
    private Object rawResponse;

    public enum ResponseStatus {
        SUCCESS,
        PENDING,
        FAILED,
        TIMEOUT
    }

    public boolean isSuccessful() {
        return status == ResponseStatus.SUCCESS;
    }

    public boolean isFailed() {
        return status == ResponseStatus.FAILED;
    }

    public boolean isPending() {
        return status == ResponseStatus.PENDING;
    }
}