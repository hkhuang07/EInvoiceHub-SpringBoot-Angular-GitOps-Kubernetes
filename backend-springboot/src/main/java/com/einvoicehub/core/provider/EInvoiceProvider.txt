package com.einvoicehub.core.provider;

import com.einvoicehub.core.provider.model.InvoiceData;
import com.einvoicehub.core.provider.model.InvoiceResult;

public interface EInvoiceProvider {

    String getProviderType();
    // Phát hành
    InvoiceResult createInvoice(int commandType, InvoiceData invoiceData);
    InvoiceResult createReplacementInvoice(InvoiceData invoiceData);
    InvoiceResult createAdjustmentInvoice(InvoiceData invoiceData);
    InvoiceResult signInvoiceByHsm(String invoiceGuid);

    //Cập nhật & Hủy
    InvoiceResult updateInvoiceByGuid(String invoiceGuid, InvoiceData invoiceData);
    InvoiceResult updateInvoiceByPartnerId(String partnerInvoiceId, InvoiceData invoiceData);
    InvoiceResult cancelInvoiceByGuid(String invoiceGuid, String reason);
    InvoiceResult cancelInvoiceByPartnerId(String partnerInvoiceId, String reason);
    InvoiceResult deleteInvoiceByGuid(String invoiceGuid);
    InvoiceResult deleteInvoiceByPartnerId(String partnerInvoiceId);

    // Tra cứu & Lấy dữ liệu
    InvoiceResult getInvoiceData(String invoiceGuid);
    Object getInvoiceStatus(String invoiceGuid);
    String getInvoicePdf(String docId);
    String getInvoiceXml(String docId);
    String getInvoiceLink(String partnerInvoiceId);

    //Tiện ích
    Object lookupCompany(String taxCode);
}