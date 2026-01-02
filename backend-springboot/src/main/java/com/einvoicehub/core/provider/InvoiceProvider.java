package com.einvoicehub.core.provider;

import com.einvoicehub.core.entity.enums.InvoiceStatus;

/**
 * Interface định nghĩa contract chung cho các Provider hóa đơn điện tử
 * Áp dụng Adapter Pattern để đồng nhất cách giao tiếp với các Provider khác nhau
 */
public interface InvoiceProvider {

    /**
     * Lấy mã Provider
     * @return Mã provider (VD: VNPT, VIETTEL)
     */
    String getProviderCode();

    String getProviderName();

    boolean isAvailable();

    /**
     * Phát hành hóa đơn mới
     * @param request Thông tin yêu cầu phát hành
     * @param config Cấu hình kết nối Provider
     * @return Phản hồi từ Provider
     */
    InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config);

    /**
     * Lấy trạng thái hóa đơn
     * @param invoiceNumber Số hóa đơn
     * @param config Cấu hình kết nối Provider
     * @return Trạng thái hóa đơn
     */
    InvoiceStatus getInvoiceStatus(String invoiceNumber, ProviderConfig config);

    /**
     * Hủy hóa đơn
     * @param invoiceNumber Số hóa đơn cần hủy
     * @param reason Lý do hủy
     * @param config Cấu hình kết nối Provider
     * @return Phản hồi từ Provider
     */
    InvoiceResponse cancelInvoice(String invoiceNumber, String reason, ProviderConfig config);

    /**
     * Thay thế hóa đơn
     * @param oldInvoiceNumber Số hóa đơn cũ
     * @param newRequest Thông tin hóa đơn mới
     * @param config Cấu hình kết nối Provider
     * @return Phản hồi từ Provider
     */
    InvoiceResponse replaceInvoice(String oldInvoiceNumber, InvoiceRequest newRequest, ProviderConfig config);

    /**
     * Lấy file PDF hóa đơn
     * @param invoiceNumber Số hóa đơn
     * @param config Cấu hình kết nối Provider
     * @return URL hoặc dữ liệu PDF
     */
    String getInvoicePdf(String invoiceNumber, ProviderConfig config);

    /**
     * Xác thực và lấy token (nếu Provider yêu cầu)
     * @param config Cấu hình kết nối Provider
     * @return Token xác thực
     */
    String authenticate(ProviderConfig config);

    /**
     * Kiểm tra kết nối Provider
     * @param config Cấu hình kết nối Provider
     * @return true nếu kết nối thành công
     */
    boolean testConnection(ProviderConfig config);
}