package com.einvoicehub.core.provider.impl.vnpt;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.provider.model.InvoiceRequest;
import com.einvoicehub.core.provider.model.InvoiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class VnptApiMapper {

    /**
     * Chuyển đổi InvoiceRequest sang Payload cho API VNPT
     */
    public InvoiceResponse toHubResponse(VnptAdapter.VnptApiResponse vnptResponse, String requestId) {
        if (vnptResponse == null) {
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorMessage("No response from VNPT")
                    .responseTime(LocalDateTime.now())
                    .build();
        }

        // VNPT trả về success là boolean
        boolean isSuccess = vnptResponse.isSuccess();

        return InvoiceResponse.builder()
                .clientRequestId(requestId)
                .status(isSuccess ? InvoiceResponse.ResponseStatus.SUCCESS :
                        InvoiceResponse.ResponseStatus.FAILED)
                .errorCode(vnptResponse.getErrorCode())
                .errorMessage(vnptResponse.getMessage())
                .transactionCode(vnptResponse.getTransactionId())
                .invoiceNumber(vnptResponse.getInvoiceNumber())
                // fkey của VNPT thường chứa thông tin ký hiệu và mẫu số
                .symbolCode(vnptResponse.getFkey() != null && vnptResponse.getFkey().length() > 10 ?
                        vnptResponse.getFkey().substring(0, 10) : vnptResponse.getFkey())
                .templateCode(vnptResponse.getFkey())
                .lookupCode(vnptResponse.getLookupCode())
                .securityCode(vnptResponse.getSecurityCode())
                .pdfUrl(vnptResponse.getPdfUrl())
                .responseTime(LocalDateTime.now())
                .rawResponse(vnptResponse)
                .build();
    }

    public VnptAdapter.VnptInvoicePayload toVnptPayload(InvoiceRequest request) {
        List<VnptAdapter.VnptInvoiceDetail> details = request.getItems().stream()
                .map(this::convertToVnptDetail)
                .collect(Collectors.toList());

        return VnptAdapter.VnptInvoicePayload.builder()
                .invoiceType(request.getInvoiceType() != null ? request.getInvoiceType() : "01GTKT")
                .templateCode(request.getExtraConfig() != null ?
                        (String) request.getExtraConfig().getOrDefault("templateCode", "01GTKT0/001") : "01GTKT0/001")
                .issueDate(request.getIssueDate() != null ?
                        request.getIssueDate().format(DateTimeFormatter.ISO_DATE) :
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))
                .seller(VnptAdapter.VnptParty.builder()
                        .taxCode(request.getSeller().getTaxCode())
                        .companyName(request.getSeller().getName())
                        .address(request.getSeller().getAddress())
                        .phone(request.getSeller().getPhone())
                        .email(request.getSeller().getEmail())
                        .build())
                .buyer(VnptAdapter.VnptParty.builder()
                        .taxCode(request.getBuyer().getTaxCode())
                        .companyName(request.getBuyer().getName())
                        .address(request.getBuyer().getAddress())
                        .phone(request.getBuyer().getPhone())
                        .email(request.getBuyer().getEmail())
                        .build())
                .details(details)
                .summary(VnptAdapter.VnptSummary.builder()
                        .subtotalAmount(request.getSummary().getSubtotalAmount())
                        .totalDiscountAmount(request.getSummary().getTotalDiscountAmount())
                        .totalTaxAmount(request.getSummary().getTotalTaxAmount())
                        .totalAmount(request.getSummary().getTotalAmount())
                        .build())
                .build();
    }

    private VnptAdapter.VnptInvoiceDetail convertToVnptDetail(InvoiceRequest.InvoiceItem item) {
        return VnptAdapter.VnptInvoiceDetail.builder()
                .itemName(item.getItemName())
                .unitName(item.getUnitName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .amount(item.getAmount())
                .discountAmount(item.getDiscountAmount())
                .taxRate(item.getTaxRate())
                .taxCategory(item.getTaxCategory() != null ? item.getTaxCategory() : "5")
                .description(item.getDescription())
                .build();
    }

    public InvoiceStatus mapVnptStatus(String vnptStatus) {
        if (vnptStatus == null) return InvoiceStatus.FAILED;
        return switch (vnptStatus.toUpperCase()) {
            case "ISSUED", "SUCCESS" -> InvoiceStatus.SUCCESS;
            case "CANCELLED" -> InvoiceStatus.CANCELLED;
            case "REPLACED" -> InvoiceStatus.REPLACED;
            default -> InvoiceStatus.FAILED;
        };
    }
}