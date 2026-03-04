package vn.softz.app.einvoicehub.provider;

import vn.softz.app.einvoicehub.provider.model.InvoiceData;
import vn.softz.app.einvoicehub.provider.model.InvoiceResult;

public interface EInvoiceProvider {

    String getProviderType();
 
    InvoiceResult createInvoice(int commandType, InvoiceData invoiceData);

    InvoiceResult updateInvoiceByGuid(String invoiceGuid, InvoiceData invoiceData);

    InvoiceResult updateInvoiceByPartnerId(String partnerInvoiceId, InvoiceData invoiceData);

    InvoiceResult cancelInvoiceByGuid(String invoiceGuid, String reason);

    InvoiceResult cancelInvoiceByPartnerId(String partnerInvoiceId, String reason);

    InvoiceResult deleteInvoiceByGuid(String invoiceGuid);

    InvoiceResult deleteInvoiceByPartnerId(String partnerInvoiceId);

    Object getInvoiceStatus(String invoiceGuid);
 
    String getInvoicePdf(String docId);

    String getInvoiceXml(String docId);

    String getInvoiceLink(String partnerInvoiceId);

    InvoiceResult createReplacementInvoice(InvoiceData invoiceData);

    InvoiceResult createAdjustmentInvoice(InvoiceData invoiceData);

    InvoiceResult signInvoiceByHsm(String invoiceGuid); // 205

    InvoiceResult getInvoiceData(String lid, String invoiceGuid);
    
    Object lookupCompany(String taxCode);
}
