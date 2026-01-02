package com.einvoicehub.adapter;

import com.einvoicehub.entity.InvoiceMetadata;
import com.einvoicehub.model.request.InvoiceRequest;
import com.einvoicehub.model.response.InvoiceResponse;

/**
 * Invoice Provider Interface - Adapter Pattern
 * 
 * Interface chuẩn hóa cho tất cả các Provider adapters
 * Mỗi provider (VNPT, Viettel, BKAV, MISA) sẽ implement interface này
 */
public interface InvoiceProvider {

    /**
     * Lấy mã provider
     */
    String getProviderCode();

    /**
     * Lấy tên provider
     */
    String getProviderName();

    /**
     * Phát hành hóa đơn
     * 
     * @param request Yêu cầu xuất hóa đơn chuẩn hóa
     * @param metadata Metadata của hóa đơn
     * @return InvoiceResponse Kết quả từ provider
     */
    InvoiceResponse issueInvoice(InvoiceRequest request, InvoiceMetadata metadata);

    /**
     * Lấy trạng thái hóa đơn
     * 
     * @param providerTransactionId ID giao dịch từ provider
     * @param metadata Metadata của hóa đơn
     * @return InvoiceResponse Trạng thái hóa đơn
     */
    InvoiceResponse getInvoiceStatus(String providerTransactionId, InvoiceMetadata metadata);

    /**
     * Hủy hóa đơn
     * 
     * @param providerTransactionId ID giao dịch từ provider
     * @param reason Lý do hủy
     * @param metadata Metadata của hóa đơn
     * @return InvoiceResponse Kết quả hủy
     */
    InvoiceResponse cancelInvoice(String providerTransactionId, String reason, InvoiceMetadata metadata);

    /**
     * Điều chỉnh hóa đơn
     * 
     * @param providerTransactionId ID giao dịch từ provider
     * @param adjustmentData Dữ liệu điều chỉnh
     * @param metadata Metadata của hóa đơn
     * @return InvoiceResponse Kết quả điều chỉnh
     */
    InvoiceResponse adjustInvoice(String providerTransactionId, Object adjustmentData, InvoiceMetadata metadata);

    /**
     * Kiểm tra kết nối với provider
     * 
     * @return true nếu kết nối thành công
     */
    boolean checkConnection();

    /**
     * Lấy thông tin template hóa đơn
     * 
     * @param metadata Metadata của hóa đơn
     * @return Danh sách template
     */
    default Object getInvoiceTemplates(InvoiceMetadata metadata) {
        return null;
    }
}