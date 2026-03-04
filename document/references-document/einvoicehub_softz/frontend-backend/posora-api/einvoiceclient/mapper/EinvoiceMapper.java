package vn.softz.app.einvoiceclient.mapper;

import org.springframework.stereotype.Component;
import vn.softz.app.biz.invoice.domain.entity.InvoiceEntity;
import vn.softz.app.biz.retail.domain.entity.RetailEntity;
import vn.softz.app.biz.retail_detail.domain.entity.RetailDetailEntity;
import vn.softz.app.einvoiceclient.dto.hub.SubmitInvoiceRequest;
import vn.softz.app.einvoiceclient.dto.hub.SubmitInvoiceDetailRequest;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.time.Instant;

@Component
public class EinvoiceMapper {

    public SubmitInvoiceRequest toHubSubmitRequest(
            RetailEntity retail,
            List<RetailDetailEntity> details,
            InvoiceEntity invoice,
            Integer submitType) {
        
        return SubmitInvoiceRequest.builder()
                .submitInvoiceType(submitType)
                .partnerInvoiceId(invoice.getId())
                .invoiceTypeId(0)
                .invoiceDate(formatDate(retail.getDate()))
                .invoiceForm(invoice != null ? invoice.getInvoiceForm() : null)
                .invoiceSeries(invoice != null ? invoice.getInvoiceSeries() : null)
                .paymentMethodId(1)
                .buyerTaxCode(invoice != null ? invoice.getInvoiceTaxCode() : null)
                .buyerCompany(invoice != null ? invoice.getInvoiceCompany() : null)
                .buyerName(invoice != null ? invoice.getInvoiceName() : null)
                .buyerAddress(invoice != null ? invoice.getInvoiceAddress() : null)
                .buyerIdNo(invoice.getInvoiceIdNo())
                .buyerMobile(invoice != null ? invoice.getInvoicePhone() : null)
                .buyerBankAccount(invoice != null ? invoice.getInvoiceBankAccount() : null)
                .buyerBankName("") // biz_invoice ko có field này...
                .buyerBudgetCode("")
                .receiverTypeId(1) // 1: email, s: sms, 
                .receiverEmail(invoice != null ? invoice.getInvoiceEmail() : null)
                .currencyCode("VND")
                .exchangeRate(1.0)
                .notes(retail.getNotes())
                .details(mapDetails(details))
                .build();
    }

    private List<SubmitInvoiceDetailRequest> mapDetails(List<RetailDetailEntity> details) {
        return details.stream()
                .map(this::mapDetail)
                .collect(Collectors.toList());
    }

    private SubmitInvoiceDetailRequest mapDetail(RetailDetailEntity detail) {
        return SubmitInvoiceDetailRequest.builder()
                .itemTypeId(detail.getTotalAmount() == BigDecimal.ZERO ? 3 : 1)
                .itemCode(detail.getProductBarcodeId())
                .itemName(detail.getProductBarcode() != null ? detail.getProductBarcode().getProductName() : null)
                .unitName(detail.getProductBarcode() != null && detail.getProductBarcode().getUnit() != null 
                        ? detail.getProductBarcode().getUnit().getUnitName() : null)
                .quantity(detail.getQuantity())
                .price(detail.getNetPrice())
                .grossAmount(detail.getNetAmount())
                .discountRate(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .taxTypeId(detail.getTaxTypeId())
                .taxRate(detail.getVat())
                .taxAmount(detail.getTaxAmount())
                .build();
    }

    // method
    private String formatDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}