package vn.softz.app.einvoicehub.provider;

import vn.softz.app.einvoicehub.provider.model.InvoiceData;
import vn.softz.app.einvoicehub.provider.model.InvoiceResult;
import vn.softz.app.einvoicehub.provider.model.InvoiceStatusResult;

import java.util.List;

public interface EInvoiceProvider {

    /** GetInvoice + GetStatusInvoice */
    InvoiceResult getInvoiceData(String lid, String invoiceGuid);
    InvoiceStatusResult getInvoiceStatusFull(String lid, String invoiceGuid);

    Object getInvoiceStatus(String lid, String invoiceGuid);
    Object lookupCompany(String lid, String taxCode);

    String getInvoiceLink(String lid, String partnerInvoiceId);
    String getInvoicePdf(String lid, String docId);
    String getInvoiceXml(String lid, String docId);

    /**Create Invoice*/
    InvoiceResult createInvoice(String lid, int commandType, InvoiceData invoiceData);

    /** Edit Invoice + Edit Adjust Invoice*/

    /** Update Invoice */
    InvoiceResult updateInvoiceByGuid(String lid, String invoiceGuid, InvoiceData invoiceData);
    InvoiceResult updateInvoiceByPartnerId(String lid, String partnerInvoiceId, InvoiceData invoiceData);

    /** Sign Invoice */
    InvoiceResult signInvoiceByHsm(String lid, String invoiceGuid);
    InvoiceResult signInvoiceBatchByHsm(String lid, List<String> invoiceGuids);

    /**Adjust Invoice*/
    InvoiceResult createAdjustmentInvoice(String lid, int submitType, InvoiceData invoiceData);
    //InvoiceResult createAdjustmentInvoice(String lid, InvoiceData invoiceData);

    /** Replace Invoice */
    InvoiceResult createReplacementInvoice(String lid, InvoiceData invoiceData);

    /** Delete Invoice */
    InvoiceResult deleteInvoiceByGuid(String lid, String invoiceGuid);
    InvoiceResult deleteInvoiceByPartnerId(String lid, String partnerInvoiceId);

    /** Cancel Invoice - theo quy định mới của nhà nước thì từ 01/07/2025 không còn Hủy hóa đơn, nên chức năng này không dùng
     InvoiceResult cancelInvoiceByGuid(String lid, String invoiceGuid, String reason);
     InvoiceResult cancelInvoiceByPartnerId(String lid, String partnerInvoiceId, String reason) ;
    */

}
