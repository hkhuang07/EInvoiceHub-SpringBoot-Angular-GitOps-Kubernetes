package vn.softz.app.einvoicehub.provider.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResult {
    // Success/Error flags
    private boolean success;
    private String message;
    
    // Partner identification
    private String partnerInvoiceId;
    private String partnerInvoiceStringId;
    
    // Invoice identification from provider
    private UUID invoiceGuid;
    private String invoiceId;           // ID từ provider (MobiFone: hdon_id)
    private String invoiceForm;        // Mẫu số
    private String invoiceSerial;      // Ký hiệu
    private String invoiceNo;         // Số hóa đơn (số)
    
    // BKAV specific fields
    private String invoiceReferenceCode;                // Mã tra cứu (https://tracuu.ehoadon.vn)
    private String taxAuthorityCode;           // Mã của CQT (cho máy tính tiền)
    
    // Status
    private Integer status;            // 0 = success, 1 = error
    private String statusMessage;      // Human-readable status
    private String messLog;            // Log message from provider
    
    private Integer invoiceTypeId;     // Hub's internal invoice type ID
    private Integer invoiceStatusId;   // Hub's internal invoice status ID
    private Integer paymentMethodId;   // Hub's internal payment method ID
    
    // Full invoice data
    private Object object;             // Detailed invoice data
    
    private Instant invoiceDate;    // Ngày hóa đơn
    private Instant signedDate;     // Ngày ký
    private String providerInvoiceId;         // ID hóa đơn từ provider (GUID string)

    public static InvoiceResult success(UUID invoiceGuid, String message) {
        return InvoiceResult.builder()
                .success(true)
                .invoiceGuid(invoiceGuid)
                .message(message)
                .status(0)
                .build();
    }
    
    public static InvoiceResult error(String message) {
        return InvoiceResult.builder()
                .success(false)
                .message(message)
                .status(1)
                .build();
    }
}
