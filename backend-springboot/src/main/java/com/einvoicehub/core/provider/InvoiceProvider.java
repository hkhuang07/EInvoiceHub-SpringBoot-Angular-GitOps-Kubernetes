package com.einvoicehub.core.provider;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.provider.model.InvoiceRequest;
import com.einvoicehub.core.provider.model.InvoiceResponse;

/**
 * Interface chuẩn cho các Adapter Provider - Bổ sung nghiệp vụ XML
 */
public interface InvoiceProvider {

    String getProviderCode();
    String getProviderName();
    boolean isAvailable();

    /** Phát hành hóa đơn */
    InvoiceResponse issueInvoice(InvoiceRequest request, ProviderConfig config);

    /** Tra cứu trạng thái */
    InvoiceStatus getInvoiceStatus(String invoiceNumber, ProviderConfig config);

    /** Hủy hóa đơn */
    InvoiceResponse cancelInvoice(String invoiceNumber, String reason, ProviderConfig config);

    /** Thay thế hóa đơn */
    InvoiceResponse replaceInvoice(String oldInvoiceNumber, InvoiceRequest newRequest, ProviderConfig config);

    /** Lấy file PDF (Để xem và in) */
    String getInvoicePdf(String invoiceNumber, ProviderConfig config);

    /** * Lấy dữ liệu XML (Gốc pháp lý của hóa đơn) - MỚI
     * @return Chuỗi XML hoặc Base64 của file XML
     */
    String getInvoiceXml(String invoiceNumber, ProviderConfig config);

    /** Xác thực */
    String authenticate(ProviderConfig config);

    /** Kiểm tra kết nối */
    boolean testConnection(ProviderConfig config);
}