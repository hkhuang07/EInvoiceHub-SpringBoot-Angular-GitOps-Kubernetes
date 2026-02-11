package com.einvoicehub.core.provider.model;

import lombok.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

/**
 * DTO phản hồi từ Provider - Đã chuẩn hóa dữ liệu trả về.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {

    private String clientRequestId;
    private Long invoiceMetadataId;
    private ResponseStatus status;

    private String errorCode;
    private String errorMessage;

    /** Mã định danh giao dịch phía Provider */
    private String transactionCode;
    private String invoiceNumber;
    private String symbolCode;
    private String templateCode;

    /** Đồng bộ kiểu LocalDateTime để xử lý múi giờ chính xác */
    private LocalDateTime issueDate;

    private String lookupCode;   // Mã tra cứu
    private String securityCode; // Mã xác thực/chữ ký số

    private String pdfUrl;
    private String xmlData;      // Dữ liệu XML gốc (pháp lý)
    private String portalUrl;    // Link tra cứu hóa đơn trực tiếp của hãng

    private String cqtCode;
    private String xmlUrl;
    private String jsonUrl;
    private boolean successful;

    private LocalDateTime responseTime;
    private Object rawResponse;  // Dữ liệu thô dùng cho debug/audit

    public enum ResponseStatus {
        SUCCESS, PENDING, FAILED, TIMEOUT
    }

    public boolean isSuccessful() { return status == ResponseStatus.SUCCESS; }
    public boolean isFailed() { return status == ResponseStatus.FAILED; }
    public boolean isPending() { return status == ResponseStatus.PENDING; }
}