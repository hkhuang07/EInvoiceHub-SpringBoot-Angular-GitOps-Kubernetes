package com.einvoicehub.core.provider.vnpt;

import com.einvoicehub.core.provider.InvoiceRequest;
import com.einvoicehub.core.provider.InvoiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
                        .bankAccount(request.getSeller().getBankAccount())
                        .bankName(request.getSeller().getBankName())
                        .representativeName(request.getSeller().getRepresentativeName())
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
                // Sử dụng taxAmount đã được thêm vào InvoiceRequest
                .taxCategory(item.getTaxCategory() != null ? item.getTaxCategory() : "5")
                .description(item.getDescription())
                .build();
    }

    public InvoiceResponse toInvoiceResponse(VnptAdapter.VnptApiResponse vnptResponse, String clientRequestId) {
        boolean isSuccess = vnptResponse != null && vnptResponse.isSuccess();

        return InvoiceResponse.builder()
                .clientRequestId(clientRequestId)
                .status(isSuccess ? InvoiceResponse.ResponseStatus.SUCCESS : InvoiceResponse.ResponseStatus.FAILED)
                .errorCode(vnptResponse != null ? vnptResponse.getErrorCode() : "NULL_RESPONSE")
                .errorMessage(vnptResponse != null ? vnptResponse.getMessage() : "No response from VNPT")
                .transactionCode(isSuccess ? vnptResponse.getTransactionId() : null)
                .invoiceNumber(isSuccess ? vnptResponse.getInvoiceNumber() : null)
                .pdfUrl(isSuccess ? vnptResponse.getPdfUrl() : null)
                .responseTime(LocalDateTime.now())
                .rawResponse(vnptResponse)
                .build();
    }
}