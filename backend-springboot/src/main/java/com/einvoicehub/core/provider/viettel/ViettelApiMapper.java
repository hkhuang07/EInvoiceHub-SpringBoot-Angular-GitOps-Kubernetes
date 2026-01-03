package com.einvoicehub.core.provider.viettel;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.provider.InvoiceRequest;
import com.einvoicehub.core.provider.InvoiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode; // Đã fix vị trí import chuẩn
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViettelApiMapper {

    /**
     * Khôi phục 100% logic buildViettelPayload từ file gốc của Huy
     */
    public ViettelAdapter.ViettelInvoicePayload toViettelPayload(InvoiceRequest request) {
        List<ViettelAdapter.ViettelInvoiceItem> items = request.getItems().stream()
                .map(this::convertToViettelItem)
                .collect(Collectors.toList());

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
                        .method(request.getPaymentTerm() != null ? request.getPaymentTerm().getMethod() : "TM/CC")
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
                // FIX: Sử dụng taxAmount đã được Huy thêm vào DTO InvoiceRequest
                .vatAmount(item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO)
                .description(item.getDescription())
                .build();
    }

    private String getInvoiceTypeCode(InvoiceRequest request) {
        if (request.getInvoiceType() != null) return request.getInvoiceType();
        if (request.getExtraConfig() != null && request.getExtraConfig().containsKey("invoiceType")) {
            return (String) request.getExtraConfig().get("invoiceType");
        }
        return "01";
    }

    private String getTemplateCode(InvoiceRequest request) {
        if (request.getExtraConfig() != null && request.getExtraConfig().containsKey("templateCode")) {
            return (String) request.getExtraConfig().get("templateCode");
        }
        return "01GTKT0/001";
    }

    /**
     * Khôi phục 100% logic convertToInvoiceResponse của Huy
     */
    public InvoiceResponse toHubResponse(ViettelAdapter.ViettelApiResponse res, String requestId) {
        if (res == null) {
            return InvoiceResponse.builder()
                    .status(InvoiceResponse.ResponseStatus.FAILED)
                    .errorMessage("No response from Viettel")
                    .responseTime(LocalDateTime.now())
                    .build();
        }

        boolean isSuccess = "200".equals(res.getCode());
        return InvoiceResponse.builder()
                .clientRequestId(requestId)
                .status(isSuccess ? InvoiceResponse.ResponseStatus.SUCCESS : InvoiceResponse.ResponseStatus.FAILED)
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
        String viettelStatus = (response != null && response.getData() != null)
                ? response.getData().getStatus() : null;

        if (viettelStatus == null) return InvoiceStatus.FAILED;

        return switch (viettelStatus.toUpperCase()) {
            case "DA_PHAT_HANH", "SIGNED" -> InvoiceStatus.SUCCESS;
            case "DA_HUY" -> InvoiceStatus.CANCELLED;
            default -> InvoiceStatus.FAILED;
        };
    }
}