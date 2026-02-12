package vn.softz.app.einvoicehub.provider.mobifone.constant;

public class MobifoneInvoiceStatus {
    
    // Invoice status (tthdon)
    public static final int ORIGINAL = 0;
    public static final int EXPLANATION = 1;
    public static final int REPLACEMENT = 2;
    public static final int CANCELLED = 3;
    public static final int REVIEW = 4;
    public static final int ADJUSTMENT = 5;
    public static final int PENDING_ADJUSTMENT = 7;
    public static final int ADJUSTED = 11;
    public static final int PENDING_CANCEL = 13;
    public static final int PENDING_REPLACEMENT = 15;
    public static final int REPLACED = 17;
    public static final int ADJUSTMENT_INCREASE = 19;
    public static final int ADJUSTMENT_DECREASE = 21;
    public static final int ADJUSTMENT_INFO = 23;
    
    // CQT status (tthai)
    public static final String STATUS_WAITING_SIGN = "Chờ ký";
    public static final String STATUS_SIGNED = "Đã ký";
    public static final String STATUS_WAITING_CODE = "Chờ cấp mã";
    public static final String STATUS_INVALID_FORMAT = "TBSS sai định dạng";
    public static final String STATUS_CODE_DENIED = "CQT Không cấp mã";
    public static final String STATUS_WAITING_RESPONSE = "Chờ phản hồi";
    public static final String STATUS_NOT_ACCEPTED = "CQT không tiếp nhận HĐ";
    public static final String STATUS_ACCEPT_TBSS = "Chấp nhận TBSS";
    public static final String STATUS_SENT = "Đã gửi";
    public static final String STATUS_RECEIVED = "CQT đã nhận";
    public static final String STATUS_CODE_GRANTED = "Đã cấp mã";
    public static final String STATUS_REJECT_TBSS = "Không chấp nhận TBSS";
    
    // Item type (kmai)
    public static final int ITEM_GOODS_SERVICE = 1;
    public static final int ITEM_PROMOTION = 2;
    public static final int ITEM_DISCOUNT = 3;
    public static final int ITEM_NOTE = 4;
    public static final int ITEM_SPECIAL = 5;
    
    // Tax rate (tsuat)
    public static final String TAX_RATE_10 = "10";
    public static final String TAX_RATE_8 = "8";
    public static final String TAX_RATE_5 = "5";
    public static final String TAX_RATE_0 = "0";
    public static final String TAX_RATE_NOT_SUBJECT = "-1";
    public static final String TAX_RATE_NOT_DECLARED = "-2";
    
    // Edit mode
    public static final int EDIT_MODE_CREATE = 1;
    public static final int EDIT_MODE_UPDATE = 2;
    public static final int EDIT_MODE_DELETE = 3;
    
    // Type command (type_cmd)
    public static final String TYPE_CMD_WITH_CODE = "200";
    public static final String TYPE_CMD_NO_CODE = "203";
    public static final String TYPE_CMD_MTT = "206";
    
    // Error notification type (tctbao)
    public static final String TBSS_CANCEL = "1";
    public static final String TBSS_REPLACE = "3";
    public static final String TBSS_EXPLANATION = "4";
    public static final String TBSS_ADJUST_INCREASE = "5";
    public static final String TBSS_ADJUST_DECREASE = "6";
    public static final String TBSS_ADJUST_OTHER = "7";
}
