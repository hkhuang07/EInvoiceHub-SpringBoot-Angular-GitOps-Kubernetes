package com.einvoicehub.core.util;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ID Generator Utility - Cập nhật theo chuẩn EInvoiceHub
 * * Thành phần hỗ trợ tạo các định danh có cấu trúc, giúp đối soát dữ liệu
 * giữa MySQL và MongoDB dễ dàng hơn so với UUID thông thường.
 */
@Component
public class IdGenerator {

    private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final SecureRandom random = new SecureRandom();
    private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Tạo ID định danh duy nhất cho mỗi yêu cầu khi tiếp nhận vào Hub.
     * Khắc phục: Đổi tên từ generateClientRequestId để phân biệt với ID của Merchant truyền lên.
     * Format: HUB-YYYYMMDD-HHMMSS-XXXXXX
     */
    public String generateHubRequestId() {
        String timestamp = LocalDateTime.now().format(timestampFormatter);
        return String.format("HUB-%s-%s", timestamp, generateRandom(6));
    }

    /**
     * Tạo mã giao dịch nội bộ để liên kết Metadata (MySQL) và Payload (MongoDB).
     * Format: TXN-YYYYMMDD-HHMMSS-XXXXXXXX
     */
    public String generateTransactionId() {
        String timestamp = LocalDateTime.now().format(timestampFormatter);
        return String.format("TXN-%s-%s", timestamp, generateRandom(8));
    }

    /**
     * Tạo số hóa đơn nội bộ (Chỉ dùng cho bản nháp DRAFT hoặc tra cứu nội bộ).
     * Cảnh báo: Số hóa đơn pháp lý sẽ do Provider (MISA, VNPT...) cấp sau khi phát hành.
     * Format: DRAFT-YYYYMMDD-XXXXXX
     */
    public String generateInternalDraftNumber() {
        String date = LocalDateTime.now().format(dateFormatter);
        return String.format("DRAFT-%s-%s", date, generateRandom(6));
    }

    /**
     * Sinh chuỗi ngẫu nhiên an toàn (Alphanumeric)
     * Sử dụng SecureRandom để đảm bảo tính bảo mật và tránh trùng lặp.
     */
    private String generateRandom(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}