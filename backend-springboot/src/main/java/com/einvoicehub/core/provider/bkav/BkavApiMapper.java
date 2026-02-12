package com.einvoicehub.core.provider.bkav;

import com.einvoicehub.core.domain.enums.InvoiceStatus;
import com.einvoicehub.core.provider.model.InvoiceRequest;
import com.einvoicehub.core.provider.model.InvoiceResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class BkavApiMapper {

    private final ObjectMapper objectMapper;

    public BkavAdapter.BkavCommandPayload toBkavPayload(InvoiceRequest request) {
        List<Map<String, Object>> details = request.getItems().stream()
                .map(this::convertToBkavDetail)
                .toList();

        Map<String, Object> invoice = new LinkedHashMap<>();
        invoice.put("InvoiceTypeID", getInvoiceTypeId(request));

        LocalDateTime issueTime = request.getIssueDate() != null ?
                request.getIssueDate().atStartOfDay() : LocalDateTime.now();
        invoice.put("InvoiceDate", issueTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));

        invoice.put("BuyerName", request.getBuyer() != null ? request.getBuyer().getName() : "");
        invoice.put("BuyerTaxCode", (request.getBuyer() != null && StringUtils.hasText(request.getBuyer().getTaxCode())) ?
                request.getBuyer().getTaxCode() : "");
        invoice.put("BuyerUnitName", request.getBuyer() != null ? request.getBuyer().getName() : "");
        invoice.put("BuyerAddress", request.getBuyer() != null ? request.getBuyer().getAddress() : "");
        invoice.put("PayMethodID", 3);
        invoice.put("ReceiveTypeID", 3);
        invoice.put("ReceiverEmail", request.getBuyer() != null ? request.getBuyer().getEmail() : "");
        invoice.put("CurrencyID", (request.getSummary() != null && StringUtils.hasText(request.getSummary().getCurrencyCode())) ?
                request.getSummary().getCurrencyCode() : "VND");
        invoice.put("ExchangeRate", 1.0);

        if (request.getExtraConfig() != null) {
            Optional.ofNullable(request.getExtraConfig().get("InvoiceForm")).ifPresent(v -> invoice.put("InvoiceForm", v));
            Optional.ofNullable(request.getExtraConfig().get("InvoiceSerial")).ifPresent(v -> invoice.put("InvoiceSerial", v));
        }

        invoice.put("InvoiceNo", 0);

        Map<String, Object> wrapper = new LinkedHashMap<>();
        wrapper.put("Invoice", invoice);
        wrapper.put("ListInvoiceDetailsWS", details);
        wrapper.put("ListInvoiceAttachFileWS", Collections.emptyList());
        wrapper.put("PartnerInvoiceID", request.getInvoiceMetadataId() != null ? request.getInvoiceMetadataId() : System.currentTimeMillis());

        int cmdType = (request.getExtraConfig() != null && request.getExtraConfig().containsKey("CmdType")) ?
                (Integer) request.getExtraConfig().get("CmdType") : 111;

        return BkavAdapter.BkavCommandPayload.builder()
                .cmdType(cmdType)
                .commandObject(List.of(wrapper))
                .build();
    }

    private Map<String, Object> convertToBkavDetail(InvoiceRequest.InvoiceItem item) {
        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("ItemName", item.getItemName());
        detail.put("UnitName", item.getUnitName() != null ? item.getUnitName() : "");
        detail.put("Qty", item.getQuantity());
        detail.put("Price", item.getUnitPrice());
        detail.put("Amount", item.getAmount());
        detail.put("TaxRateID", mapTaxRateToBkavId(item.getTaxRate()));
        detail.put("TaxRate", item.getTaxRate());
        detail.put("TaxAmount", item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO);
        detail.put("ItemTypeID", 0);
        return detail;
    }

    public InvoiceResponse toHubResponse(BkavAdapter.BkavApiResponse response, String clientRequestId) {
        if (response == null) {
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage("No response from provider").build();
        }

        boolean isSuccess = response.isOk() && !response.isError();
        return InvoiceResponse.builder()
                .clientRequestId(clientRequestId)
                .status(isSuccess ? InvoiceResponse.ResponseStatus.SUCCESS : InvoiceResponse.ResponseStatus.FAILED)
                .errorCode(String.valueOf(response.getStatus()))
                .errorMessage(isSuccess ? null : extractField(response, "MessLog"))
                .transactionCode(extractField(response, "InvoiceGUID"))
                .invoiceNumber(extractField(response, "InvoiceNo"))
                .symbolCode(extractField(response, "InvoiceSerial"))
                .lookupCode(extractField(response, "MTC"))
                .pdfUrl(extractField(response, "MessLog"))
                .responseTime(LocalDateTime.now())
                .rawResponse(response)
                .build();
    }

    public InvoiceStatus mapBkavStatus(BkavAdapter.BkavApiResponse response) {
        try {
            JsonNode node = objectMapper.readTree(objectMapper.writeValueAsString(response.getObject()));
            if (node.isArray() && !node.isEmpty()) {
                int bkavStatus = node.get(0).path("BkavStatus").asInt();
                int taxStatus = node.get(0).path("TaxStatus").asInt();

                return switch (bkavStatus) {
                    case 1, 11 -> InvoiceStatus.SIGNING;
                    case 2 -> (taxStatus == 33) ? InvoiceStatus.SUCCESS : InvoiceStatus.SENT_TO_PROVIDER;
                    case 3 -> InvoiceStatus.CANCELLED;
                    case 6 -> InvoiceStatus.REPLACED;
                    case 8 -> InvoiceStatus.SUCCESS;
                    default -> InvoiceStatus.FAILED;
                };
            }
        } catch (Exception e) { log.error("BKAV status mapping failed"); }
        return InvoiceStatus.FAILED;
    }

    private String extractField(BkavAdapter.BkavApiResponse response, String field) {
        try {
            JsonNode node = objectMapper.readTree(objectMapper.writeValueAsString(response.getObject()));
            if (node.isArray() && !node.isEmpty()) return node.get(0).path(field).asText();
        } catch (Exception ignored) {}
        return null;
    }

    private int getInvoiceTypeId(InvoiceRequest request) {
        return (request.getExtraConfig() != null && request.getExtraConfig().get("InvoiceTypeID") instanceof Integer id) ? id : 1;
    }

    private int mapTaxRateToBkavId(BigDecimal taxRate) {
        if (taxRate == null) return 1;
        double rate = taxRate.doubleValue();
        if (rate == 5) return 2;
        if (rate == 10) return 3;
        if (rate == -1) return 4;
        return 1;
    }
}