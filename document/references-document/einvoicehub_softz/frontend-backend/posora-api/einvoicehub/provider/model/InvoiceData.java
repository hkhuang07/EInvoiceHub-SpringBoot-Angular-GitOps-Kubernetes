package vn.softz.app.einvoicehub.provider.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceData {
    
    // Invoice header info
    private String lid;
    private String partnerInvoiceId;
    private String partnerInvoiceStringId;
    private Integer invoiceTypeId;
    private Instant invoiceDate;
    private String invoiceForm;
    private String invoiceSerial;
    private Integer invoiceNo;
    private Instant signedDate;
    
    // Buyer information
    private String buyerName;
    private String buyerTaxCode;
    private String buyerUnitName;
    private String buyerAddress;
    private String buyerBankAccount;
    
    // Receiver information
    private String receiverName;
    private String receiverEmail;
    private String receiverMobile;
    private String receiverAddress;
    
    // Payment information
    private Integer payMethodId;
    private Integer receiveTypeId;
    
    // Currency
    private String currencyCode;
    private BigDecimal exchangeRate;
    
    // Amounts
    private BigDecimal grossAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    
    // Notes
    private String notes;
    private String billCode;
    private String userDefine;
    
    // Provider specific
    private String providerSerialId;  // MobiFone: qlkhsdung_id / cctbao_id
    
    private String originalInvoiceIdentify;
    private Integer typeCreateInvoice;
    
    private List<InvoiceDetailData> details;
    private List<InvoiceAttachmentData> attachments;
}
