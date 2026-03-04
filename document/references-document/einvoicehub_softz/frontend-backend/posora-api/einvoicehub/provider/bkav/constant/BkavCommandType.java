package vn.softz.app.einvoicehub.provider.bkav.constant;

public class BkavCommandType {
    
    // 1xx: Create operations
    public static final int CREATE_INVOICE_MT = 100;  
    public static final int CREATE_INVOICE_TR = 101;  
    public static final int CREATE_INVOICE_WITH_FORM_SERIAL = 110;  
    public static final int CREATE_INVOICE_WITH_FORM_SERIAL_NO = 111;  
    
    public static final int CREATE_INVOICE_REPLACE = 120;  
    public static final int CREATE_INVOICE_ADJUST = 121;  
    public static final int CREATE_INVOICE_ADJUST_DISCOUNT = 122;  
    public static final int CREATE_INVOICE_REPLACE_SET_NO = 123;  
    public static final int CREATE_INVOICE_ADJUST_SET_NO = 124;
    
    // 2xx: Update operations
    public static final int UPDATE_INVOICE_BY_PARTNER_ID = 200;
    public static final int CANCEL_INVOICE_BY_GUID = 201;
    public static final int CANCEL_INVOICE_BY_PARTNER_ID = 202;
    public static final int UPDATE_INVOICE_BY_FORM_SERIAL_NO = 203;
    public static final int UPDATE_INVOICE_BY_GUID = 204;
    public static final int SIGN_INVOICE_BY_HSM = 205;  // Ký hóa đơn bằng HSM (Hardware Security Module)
    
    // 3xx: Delete operations
    public static final int DELETE_INVOICE_BY_PARTNER_ID = 301;
    public static final int DELETE_INVOICE_PROCESSING = 302;
    public static final int DELETE_INVOICE_BY_GUID = 303;
    
    // 5xx: File operations
    public static final int UPLOAD_FILE = 500;
    
    // 8xx: Query operations
    public static final int GET_INVOICE_DATA_WS = 800;  // Get invoice details
    public static final int GET_INVOICE_STATUS_ID = 801;  // Get status
    public static final int GET_INVOICE_HISTORY = 802;  // Get history
    public static final int GET_INVOICE_PDF_FILE = 803;  // Get PDF
    public static final int GET_INVOICE_LINK = 804;  // Get download link
    public static final int GET_INVOICE_XML = 805;  // Get XML for signing
    public static final int GET_LIST_INVOICE_WS_CK = 806;  // Get waiting invoices
    public static final int GET_REMAIN_INVOICE_NUM = 807;  // Get remaining invoice count
    public static final int GET_INVOICE_DATA_FILE_PDF = 808;  // Get PDF Base64
    public static final int GET_INVOICE_DATA_FILE_XML = 809;  // Get XML Base64
    public static final int GET_INVOICE_STATUS_WITH_TAX_CODE = 850;  // Get invoice status and tax authority code
    
    // 9xx: Account and utility operations
    public static final int RESEND = 901;
    public static final int CREATE_ACCOUNT = 902;
    public static final int UPDATE_ACCOUNT = 903;
    public static final int GET_UNIT_INFO_BY_TAXCODE = 904;
    public static final int UPDATE_PASSWORD = 905;
    public static final int UPDATE_ACCOUNT_STATUS = 906;
    public static final int UPDATE_EXPIRE_INFO = 907;
    public static final int GET_EXPIRE_INFO = 908;
    public static final int GET_ACCOUNT_INFO_BY_TAXCODE = 909;
    public static final int AUTHENTICATE_USER_RETURN_INVOICE_FORM_SERIAL = 910;
    public static final int RESEND_WITH_EMAIL_MOBILE = 911;
    
    // 10xx: Tool exchange
    public static final int GET_RUN_TYPE_INFO = 1000;
    public static final int GET_DLL_CONTENT = 1001;
}
