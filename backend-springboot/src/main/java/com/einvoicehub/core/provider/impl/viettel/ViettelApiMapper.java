package com.einvoicehub.core.provider.impl.viettel;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.provider.model.InvoiceRequest;
import com.einvoicehub.core.provider.model.InvoiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViettelApiMapper {

    public ViettelAdapter.ViettelInvoicePayload toViettelPayload(InvoiceRequest request) {
        List<ViettelAdapter.ViettelInvoiceItem> items = request.getItems().stream()
                .map(this::convertToViettelItem)
                .toList();

        return ViettelAdapter.ViettelInvoicePayload.builder()
                .invoiceType(getInvoiceTypeCode(request))
                .templateCode(getTemplateCode(request))
                .issueDate(request.getIssueDate() != null ?
                        request.getIssueDate().format(DateTimeFormatter.ISO_DATE) :
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))
                .seller(ViettelAdapter.ViettelParty.builder()
                        .taxCode(request.getSeller().getTaxCode())
                        .name(request.getSeller().getName())
                        .address(request.getSeller().getAddress())
                        .phone(request.getSeller().getPhone())
                        .email(request.getSeller().getEmail())
                        .bankAccount(request.getSeller().getBankAccount())
                        .bankName(request.getSeller().getBankName())
                        .contactName(request.getSeller().getRepresentativeName())
                        .build())
                .buyer(ViettelAdapter.ViettelParty.builder()
                        .taxCode(request.getBuyer().getTaxCode())
                        .name(request.getBuyer().getName())
                        .address(request.getBuyer().getAddress())
                        .phone(request.getBuyer().getPhone())
                        .email(request.getBuyer().getEmail())
                        .build())
                .items(items)
                .payment(ViettelAdapter.ViettelPayment.builder()
                        .method(request.getPaymentTerm() != null ? request.getPaymentTerm().getMethod() : "TM/CK")
                        .totalAmount(request.getSummary().getTotalAmount())
                        .currency(request.getSummary().getCurrencyCode())
                        .build())
                .build();
    }

    private ViettelAdapter.ViettelInvoiceItem convertToViettelItem(InvoiceRequest.InvoiceItem item) {
        return ViettelAdapter.ViettelInvoiceItem.builder()
                .name(item.getItemName())
                .unit(item.getUnitName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .amount(item.getAmount())
                .discountAmount(item.getDiscountAmount() != null ? item.getDiscountAmount() : BigDecimal.ZERO)
                .vatRate(item.getTaxRate())
                .vatAmount(item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO)
                .description(item.getDescription())
                .build();
    }

    private String getInvoiceTypeCode(InvoiceRequest request) {
        if (request.getInvoiceType() != null) return request.getInvoiceType();
        return (request.getExtraConfig() != null && request.getExtraConfig().containsKey("invoiceType")) ?
                (String) request.getExtraConfig().get("invoiceType") : "01";
    }

    private String getTemplateCode(InvoiceRequest request) {
        return (request.getExtraConfig() != null && request.getExtraConfig().containsKey("templateCode")) ?
                (String) request.getExtraConfig().get("templateCode") : "01GTKT0/001";
    }

    public InvoiceResponse toHubResponse(ViettelAdapter.ViettelApiResponse res, String requestId) {
        if (res == null) {
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage("Provider timeout").build();
        }

        boolean success = "200".equals(res.getCode());
        return InvoiceResponse.builder()
                .clientRequestId(requestId)
                .status(success ? InvoiceResponse.ResponseStatus.SUCCESS : InvoiceResponse.ResponseStatus.FAILED)
                .errorCode(res.getCode())
                .errorMessage(res.getMessage())
                .transactionCode(res.getTransactionId())
                .invoiceNumber(res.getData() != null ? res.getData().getInvoiceCode() : null)
                .symbolCode(res.getData() != null ? res.getData().getInvoiceNo() : null)
                .lookupCode(res.getData() != null ? res.getData().getCheckSum() : null)
                .pdfUrl(res.getData() != null ? res.getData().getPdfUrl() : null)
                .responseTime(LocalDateTime.now())
                .rawResponse(res)
                .build();
    }

    public InvoiceStatus mapViettelStatus(ViettelAdapter.ViettelApiResponse response) {
        String status = (response != null && response.getData() != null) ? response.getData().getStatus() : null;
        if (status == null) return InvoiceStatus.FAILED;

        return switch (status.toUpperCase()) {
            case "DA_PHAT_HANH", "SIGNED" -> InvoiceStatus.SUCCESS;
            case "DA_HUY" -> InvoiceStatus.CANCELLED;
            default -> InvoiceStatus.FAILED;
        };
    }
}