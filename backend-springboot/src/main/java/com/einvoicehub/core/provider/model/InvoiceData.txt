package com.einvoicehub.core.provider.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceData {

    //Header
    private String partnerInvoiceId;
    private String partnerInvoiceStringId;
    private Integer invoiceTypeId;
    private LocalDateTime invoiceDate;
    private String invoiceForm;      //Mẫu số
    private String invoiceSerial;    //serial
    private Integer invoiceNo;
    private LocalDateTime signedDate;

    //Buyer
    private String buyerName;
    private String buyerTaxCode;
    private String buyerUnitName;
    private String buyerAddress;
    private String buyerBankAccount;

    //Receiver
    private String receiverName;
    private String receiverEmail;
    private String receiverMobile;
    private String receiverAddress;

    // Payment , currency
    private Integer payMethodId;
    private Integer receiveTypeId;
    private String currencyCode;
    private BigDecimal exchangeRate;

    //Totals
    private BigDecimal grossAmount;        //tiền thô
    private BigDecimal taxAmount;          //tiền thuế
    private BigDecimal totalAmount;

    //Metadata, config
    private String notes;
    private String billCode;
    private String userDefine;
    private String providerSerialId;       // ID dải số riêng của Provider
    private String originalInvoiceIdentify; // ID gốc
    private Integer typeCreateInvoice;      // 0: Nháp, 1: Phát hành ngay

    private List<InvoiceDetailData> details;
    private List<InvoiceAttachmentData> attachments;
}