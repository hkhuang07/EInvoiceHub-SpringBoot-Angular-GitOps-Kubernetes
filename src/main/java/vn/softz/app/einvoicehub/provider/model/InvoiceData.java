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

    private String lid;

    // === Invoice header ===
    private String partnerInvoiceId;        // entity: partnerInvoiceId
    private String partnerInvoiceStringId;  // Bkav specific
    private Integer invoiceTypeId;          // entity: invoiceTypeId
    private Integer referenceTypeId;        // entity: referenceTypeId
    private String invoiceForm;             // entity: invoiceForm
    private String invoiceSerial;           // entity: invoiceSeries
    private String invoiceNo;               // entity: invoiceNo (String)
    private Instant invoiceDate;            // entity: invoiceDate
    private Instant signedDate;             // entity: signedDate

    // === Provider info ===
    private String providerId;              // entity: providerId
    private String providerInvoiceId;       // entity: providerInvoiceId
    private String providerSerialId;        // provider specific (e.g. MobiFone)
    private Integer statusId;              // entity: statusId
    private String taxAuthorityCode;        // entity: taxAuthorityCode (mã CQT)
    private String invoiceLookupCode;       // entity: invoiceLookupCode (MTC)

    // === Buyer ===
    private String buyerName;              // entity: buyerFullName
    private String buyerIdNo;             // entity: buyerIdNo (CCCD/CMND)
    private String buyerTaxCode;           // entity: buyerTaxCode
    private String buyerUnitName;          // entity: buyerCompany
    private String buyerAddress;           // entity: buyerAddress
    private String buyerMobile;            // entity: buyerMobile
    private String buyerBankAccount;       // entity: buyerBankAccount
    private String buyerBankName;          // entity: buyerBankName
    private String buyerBudgetCode;        // entity: buyerBudgetCode

    // === Receiver ===
    private String receiverName;           //
    private String receiverEmail;          // entity: receiverEmail
    private String receiverMobile;         //
    private String receiverAddress;        //
    private Integer receiveTypeId;         // entity: receiveTypeId

    // === Payment ===
    private Integer paymentMethodId;       // entity: paymentMethodId

    // === Currency ===
    private String currencyCode;           // entity: currencyCode
    private BigDecimal exchangeRate;       // entity: exchangeRate

    // === Amounts ===
    private BigDecimal grossAmount;        // entity: grossAmount
    private BigDecimal discountAmount;     // entity: discountAmount
    private BigDecimal netAmount;          // entity: netAmount
    private BigDecimal taxAmount;          // entity: taxAmount
    private BigDecimal totalAmount;        // entity: totalAmount

    // === Original invoice (dùng khi điều chỉnh/thay thế) ===
    private String orgInvoiceId;           // entity: orgInvoiceId
    private String orgInvoiceForm;         // entity: orgInvoiceForm
    private String orgInvoiceSeries;       // entity: orgInvoiceSeries
    private String orgInvoiceNo;           // entity: orgInvoiceNo
    private Instant orgInvoiceDate;        // entity: orgInvoiceDate
    private String orgInvoiceReason;       // entity: orgInvoiceReason
    private String orgInvoiceIdentify;     // provider specific

    private Boolean isIncrease;            // adjust property

    // === Extra ===
    private String note;                   // Bkav: Note
    private String billCode;               // Bkav: BillCode
    private String userDefine;             // Bkav: UserDefine
    private Integer typeCreateInvoice;     // provider specific

    // === Details & Attachments ===
    private List<InvoiceDetailData> details;
    private List<InvoiceAttachmentData> attachments;
}