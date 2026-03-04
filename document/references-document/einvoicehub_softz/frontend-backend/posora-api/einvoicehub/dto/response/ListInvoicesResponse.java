package vn.softz.app.einvoicehub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListInvoicesResponse {
    private String id;
    private String partnerInvoiceId;
    private String providerId;
    private String providerInvoiceId;    
    private String invoiceForm;
    private String invoiceSeries;
    private String invoiceNo;
    private Instant invoiceDate;    
    private String buyerTaxCode;
    private String buyerCompany;
    private String buyerFullName;
    private String buyerMobile;    
    private Integer paymentMethodId;
    private String paymentMethodName;    
    private BigDecimal grossAmount;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;    
    private Integer statusId;
    private String statusName;
    private Instant signedDate;
    private String taxAuthorityCode;    
    private Instant createdDate;
    private String createdBy;
    private String createdByUsername;
    private String createdByFullName;
    private Instant updatedDate;
    private String updatedBy;
}
