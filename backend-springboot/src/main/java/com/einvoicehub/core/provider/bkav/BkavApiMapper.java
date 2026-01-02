package com.einvoicehub.core.provider.bkav;

import com.einvoicehub.core.provider.InvoiceRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BkavApiMapper {

    private final ObjectMapper objectMapper;
    private static final DateTimeFormatter BKAV_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public BkavAdapter.BkavCommandPayload toBkavPayload(InvoiceRequest request) {
        // Lấy thông tin loại hóa đơn từ extraConfig
        String invoiceType = request.getExtraConfig() != null ?
                (String) request.getExtraConfig().getOrDefault("invoiceType", "01GTKT") : "01GTKT";

        BkavAdapter.BkavInvoiceData invoiceData = BkavAdapter.BkavInvoiceData.builder()
                .invoiceTypeId(mapInvoiceType(invoiceType))
                .invoiceDate(formatDateTime(request.getIssueDate())) // Đã sửa lỗi LocalDate
                .buyerName(request.getBuyer().getName())
                .buyerTaxCode(request.getBuyer().getTaxCode())
                .buyerAddress(request.getBuyer().getAddress())
                .receiverEmail(request.getBuyer().getEmail())
                .receiverMobile(request.getBuyer().getPhone())
                .currencyId(request.getSummary().getCurrencyCode())
                .exchangeRate(1.0)
                .payMethodId(3)
                .receiveTypeId(3)
                .build();

        List<BkavAdapter.BkavInvoiceDetail> details = request.getItems().stream()
                .map(this::convertToBkavDetail)
                .collect(Collectors.toList());

        return BkavAdapter.BkavCommandPayload.builder()
                .cmdType(111)
                .invoice(invoiceData)
                .details(details)
                .build();
    }

    private BkavAdapter.BkavInvoiceDetail convertToBkavDetail(InvoiceRequest.InvoiceItem item) {
        return BkavAdapter.BkavInvoiceDetail.builder()
                .itemName(item.getItemName())
                .unitName(item.getUnitName())
                .quantity(item.getQuantity().doubleValue())
                .unitPrice(item.getUnitPrice().doubleValue())
                .amount(item.getAmount().doubleValue())
                .taxRate(item.getTaxRate().doubleValue())
                .itemTypeID(0)
                .build();
    }

    private String mapInvoiceType(String type) {
        return "BANHANG".equalsIgnoreCase(type) ? "2" : "1";
    }

    // Sửa lỗi nhận LocalDate từ InvoiceRequest
    private String formatDateTime(LocalDate date) {
        LocalDateTime dateTime = (date != null) ? date.atStartOfDay() : LocalDateTime.now();
        return dateTime.format(BKAV_DATE_FORMAT);
    }

    public String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}