package vn.softz.app.einvoicehub.provider.mobifone.constant;

public class MobifoneApiEndpoints {
    
    // Authentication
    public static final String LOGIN = "/api/Account/Login";
    
    // System
    public static final String GET_DATA_REFERENCES = "/api/System/GetDataReferencesByRefId";
    
    // Invoice - Create/Update
    public static final String SAVE_INVOICE = "/api/Invoice68/SaveListHoadon78";
    public static final String SAVE_INVOICE_MTT = "/api/Invoice68/SaveListHoadon78MTT";
    public static final String SAVE_AND_SIGN_INVOICE = "/api/Invoice68/SaveAndSignHoadon78";
    
    // Invoice - Sign
    public static final String SIGN_INVOICE = "/api/Invoice68/SignInvoiceCertFile68";
    public static final String SIGN_INVOICE_HSM = "/api/Invoice68/SignListInvoiceCertFile_HSM";
    public static final String SEND_TO_CQT = "/api/Invoice68/SendInvoiceToCQT68";
    public static final String GET_CERTIFICATES = "/api/Invoice68/GetListCertificatesFile68";
    
    // Invoice - Query
    public static final String GET_INVOICE_BY_ID = "/api/Invoice68/GetById";
    public static final String GET_INVOICE_BY_FKEY = "/api/Invoice68/GetHoadonFkey";
    public static final String GET_INVOICE_PDF = "/api/Invoice68/inHoadon";
    public static final String GET_INVOICE_XML = "/api/Invoice68/ExportXMLHoadon";
    public static final String GET_INVOICE_LIST_BY_DATE = "/api/Invoice68/GetInvoiceFromdateTodate";
    public static final String GET_INVOICE_LIST_BY_TIME_UNIT = "/api/Invoice68/GetInvoiceByTimeAndUnit";
    public static final String GET_INVOICE_IDS_BY_DATE = "/api/Invoice68/GetListInvoiceIdFromdateTodate";
    public static final String GET_INVOICE_HISTORY = "/api/Invoice68/GetHistoryInvoice";
    public static final String GET_PENDING_INVOICES = "/api/Invoice68/GetDataInvoiceListChoKy";
    
    // Invoice - Delete
    public static final String DELETE_UNSIGNED_INVOICE = "/api/Invoice68/hoadonXoaNhieu";
    public static final String CANCEL_NO_CODE_INVOICE = "/api/Invoice68/uploadCanceledInv";
    
    // Error Notification (TBSS)
    public static final String SAVE_ERROR_NOTIFICATION = "/api/Invoice68/huyhoadonSave";
    public static final String DELETE_ERROR_NOTIFICATION = "/api/Invoice68/deleteMau04";
    public static final String SIGN_ERROR_NOTIFICATION = "/api/Invoice/huyhoadonSign";
    public static final String GET_ERROR_NOTIFICATION_PDF = "/api/Invoice68/inMau04";
    public static final String GET_ERROR_NOTIFICATION_XML = "/api/Invoice68/XmlMau0405";
    public static final String SAVE_ERROR_NOTIFICATION_XML = "/api/Invoice68/SaveXmlMau0405";
    
    // Plugin Sign
    public static final String EXPORT_XML_PRETREATMENT = "/api/Invoice68/ExportInvoiceXmlPretreatment";
    public static final String SAVE_XML_PRETREATMENT = "/api/Invoice68/SaveXmlPretreatment";
    
    // Email
    public static final String SEND_EMAIL = "/api/Invoice68/AutoSendInvoiceByEmail";
    
    // Print Multiple
    public static final String PRINT_INVOICES = "/api/Invoice68/InDanhSachHoaDon";
    
    // Reports
    public static final String GET_SALES_REPORT = "/api/Invoice/GetDSHoaDonBangKeBanRa";
    public static final String GET_DETAIL_REPORT = "/api/Invoice/GetDSHoaDonBangKeBanRaChiTiet";
    
    // Response messages
    public static final String GET_RESPONSE_MESSAGE = "/api/Invoice68/GetResponseMessage";
}
