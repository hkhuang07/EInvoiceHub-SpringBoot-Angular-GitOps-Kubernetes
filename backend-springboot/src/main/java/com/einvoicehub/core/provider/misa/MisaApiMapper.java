package com.einvoicehub.core.provider.misa;

import com.einvoicehub.core.provider.InvoiceRequest;
import com.einvoicehub.core.provider.InvoiceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MisaApiMapper {

    private static final DateTimeFormatter MISA_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Mapping từ InvoiceRequest sang MisaInvoicePayload
     */
    public MisaAdapter.MisaInvoicePayload toMisaPayload(InvoiceRequest request) {
        return MisaAdapter.MisaInvoicePayload.builder()
                .refId(java.util.UUID.randomUUID().toString())
                .invSeries((String) request.getExtraConfig().getOrDefault("invSeries", ""))
                .invDate(request.getIssueDate() != null ?
                        request.getIssueDate().atStartOfDay().format(MISA_DATE_FORMAT) :
                        LocalDateTime.now().format(MISA_DATE_FORMAT))
                .currencyCode(request.getSummary().getCurrencyCode())
                .exchangeRate(BigDecimal.ONE)
                .paymentMethodName(request.getPaymentTerm() != null ? request.getPaymentTerm().getMethod() : "TM/CK")
                .buyerLegalName(request.getBuyer().getName())
                .buyerTaxCode(request.getBuyer().getTaxCode())
                .buyerAddress(request.getBuyer().getAddress())
                .buyerEmail(request.getBuyer().getEmail())
                .buyerPhoneNumber(request.getBuyer().getPhone())
                .originalInvoiceDetail(request.getItems().stream()
                        .map(this::convertToMisaDetail)
                        .collect(Collectors.toList()))
                .build();
    }

    private MisaAdapter.MisaInvoiceDetail convertToMisaDetail(InvoiceRequest.InvoiceItem item) {
        return MisaAdapter.MisaInvoiceDetail.builder()
                .itemType(1)
                .itemName(item.getItemName())
                .unitName(item.getUnitName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .amountOC(item.getAmount())
                .discountAmountOC(item.getDiscountAmount() != null ? item.getDiscountAmount() : BigDecimal.ZERO)
                .vatRateName(mapTaxRate(item.getTaxRate()))
                .build();
    }

    private String mapTaxRate(BigDecimal rate) {
        if (rate == null) return "0%";
        return rate.stripTrailingZeros().toPlainString() + "%";
    }

    public InvoiceResponse toInvoiceResponse(MisaAdapter.MisaApiResponse response, String clientRequestId) {
        // Khắc phục lỗi getSuccess: Với kiểu boolean, Lombok tạo method isSuccess()
        boolean isSuccess = response != null && response.isSuccess();

        return InvoiceResponse.builder()
                .status(isSuccess ? InvoiceResponse.ResponseStatus.SUCCESS : InvoiceResponse.ResponseStatus.FAILED)
                .errorCode(response != null ? response.getErrorCode() : "UNKNOWN")
                .errorMessage(response != null ? response.getDescriptionErrorCode() : "No response from MISA")
                .clientRequestId(clientRequestId)
                .responseTime(LocalDateTime.now())
                .rawResponse(response)
                .build();
    }
}