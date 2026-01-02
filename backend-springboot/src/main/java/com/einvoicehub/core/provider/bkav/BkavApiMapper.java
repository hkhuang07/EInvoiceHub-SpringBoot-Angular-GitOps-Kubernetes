package com.einvoicehub.core.provider.bkav;

import com.einvoicehub.core.entity.mysql.InvoiceMetadata;
import com.einvoicehub.core.provider.InvoiceRequest;
import com.einvoicehub.core.provider.InvoiceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BkavApiMapper {

    private final ObjectMapper objectMapper;
    private static final DateTimeFormatter BKAV_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    /**
     * Chuyển đổi từ Request của Hub sang Payload của BKAV
     */
    public BkavAdapter.BkavCommandPayload toBkavPayload(InvoiceRequest request) {
        // Khởi tạo thông tin hóa đơn
        BkavAdapter.BkavInvoiceData invoiceData = BkavAdapter.BkavInvoiceData.builder()
                .invoiceTypeId(mapInvoiceType(request.getInvoiceTypeCode())) //Khắc phục lỗi: Cannot resolve method 'getInvoiceTypeCode' in 'InvoiceRequest'
                .invoiceDate(formatDateTime(request.getIssueDate())) //Khắc phục lỗi 'formatDateTime(java.time.LocalDateTime)' in 'com.einvoicehub.core.provider.bkav.BkavApiMapper' cannot be applied to '(java.time.LocalDate)'
                .buyerName(request.getBuyer().getName())
                .buyerTaxCode(request.getBuyer().getTaxCode())
                .buyerAddress(request.getBuyer().getAddress())
                .receiverEmail(request.getBuyer().getEmail())
                .receiverMobile(request.getBuyer().getPhone())
                .currencyId(request.getSummary().getCurrencyCode())
                .exchangeRate(1.0)
                .payMethodId(3) // Mặc định chuyển khoản
                .receiveTypeId(3) // Email & SMS
                .build();

        // Chuyển đổi danh sách sản phẩm
        List<BkavAdapter.BkavInvoiceDetail> details = request.getItems().stream()
                .map(this::convertToBkavDetail)
                .collect(Collectors.toList());

        return BkavAdapter.BkavCommandPayload.builder()
                .cmdType(111) // Mặc định lệnh tạo hóa đơn
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
        return "GTGT".equalsIgnoreCase(type) ? "1" : "1";
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return (dateTime != null ? dateTime : LocalDateTime.now()).format(BKAV_DATE_FORMAT);
    }

    public String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}