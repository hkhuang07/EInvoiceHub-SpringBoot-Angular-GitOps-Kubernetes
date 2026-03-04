package com.einvoicehub.core.provider.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResult {
    private boolean success;
    private String message;

    //Identification
    private String partnerInvoiceId;
    private String partnerInvoiceStringId;
    private UUID invoiceGuid;           //UUID
    private String invoiceId;           //ID từ provider
    private String providerInvoiceId;

    //Invoice
    private String invoiceForm;
    private String invoiceSerial;
    private String invoiceNo;

    //Provider Specific (BKAV/CQT)
    private String invoiceReferenceCode; //Mã tra cứu
    private String taxAuthorityCode;    //Mã của Cơ quan Thuế

    //Status
    private Integer status;             // 0: Success, 1: Error
    private String statusMessage;
    private String messLog;
    private LocalDateTime invoiceDate;
    private LocalDateTime signedDate;

    private Object object;              // Object || JSON || XML


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