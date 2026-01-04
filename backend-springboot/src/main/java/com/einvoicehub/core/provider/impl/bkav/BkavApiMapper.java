package com.einvoicehub.core.provider.impl.bkav;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
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
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BkavApiMapper {

    private final ObjectMapper objectMapper;

    /**
     * Khôi phục chính xác logic buildBkavPayload từ file gốc của Huy
     */
    public BkavAdapter.BkavCommandPayload toBkavPayload(InvoiceRequest request) {
        List<Map<String, Object>> details = request.getItems().stream()
                .map(this::convertToBkavDetail)
                .collect(Collectors.toList());

        Map<String, Object> invoiceData = new LinkedHashMap<>();
        invoiceData.put("InvoiceTypeID", getInvoiceTypeId(request));

        // FIX: Chuyển LocalDate sang LocalDateTime để format không lỗi
        LocalDateTime issueDateTime = request.getIssueDate() != null ?
                request.getIssueDate().atStartOfDay() : LocalDateTime.now();
        invoiceData.put("InvoiceDate", formatDateTime(issueDateTime));

        invoiceData.put("BuyerName", request.getBuyer() != null ? request.getBuyer().getName() : "");
        invoiceData.put("BuyerTaxCode", (request.getBuyer() != null && StringUtils.hasText(request.getBuyer().getTaxCode())) ?
                request.getBuyer().getTaxCode() : "");
        invoiceData.put("BuyerUnitName", request.getBuyer() != null ? request.getBuyer().getName() : "");
        invoiceData.put("BuyerAddress", request.getBuyer() != null ? request.getBuyer().getAddress() : "");
        invoiceData.put("BuyerBankAccount", request.getBuyer() != null ? request.getBuyer().getBankAccount() : "");
        invoiceData.put("PayMethodID", 3);
        invoiceData.put("ReceiveTypeID", 3);
        invoiceData.put("ReceiverEmail", request.getBuyer() != null ? request.getBuyer().getEmail() : "");
        invoiceData.put("ReceiverMobile", request.getBuyer() != null ? request.getBuyer().getPhone() : "");
        invoiceData.put("Note", "");
        invoiceData.put("BillCode", request.getClientRequestId() != null ? request.getClientRequestId() : "");
        invoiceData.put("CurrencyID", (request.getSummary() != null && StringUtils.hasText(request.getSummary().getCurrencyCode())) ?
                request.getSummary().getCurrencyCode() : "VND");
        invoiceData.put("ExchangeRate", 1.0);

        if (request.getExtraConfig() != null) {
            if (request.getExtraConfig().containsKey("InvoiceForm")) invoiceData.put("InvoiceForm", request.getExtraConfig().get("InvoiceForm"));
            if (request.getExtraConfig().containsKey("InvoiceSerial")) invoiceData.put("InvoiceSerial", request.getExtraConfig().get("InvoiceSerial"));
        }

        invoiceData.put("InvoiceNo", 0);

        Map<String, Object> invoiceWrapper = new LinkedHashMap<>();
        invoiceWrapper.put("Invoice", invoiceData);
        invoiceWrapper.put("ListInvoiceDetailsWS", details);
        invoiceWrapper.put("ListInvoiceAttachFileWS", Collections.emptyList());
        invoiceWrapper.put("PartnerInvoiceID", request.getInvoiceMetadataId() != null ? request.getInvoiceMetadataId() : System.currentTimeMillis());
        invoiceWrapper.put("PartnerInvoiceStringID", "");

        int cmdType = (request.getExtraConfig() != null && request.getExtraConfig().containsKey("CmdType")) ?
                (Integer) request.getExtraConfig().get("CmdType") : 111;

        return BkavAdapter.BkavCommandPayload.builder()
                .cmdType(cmdType)
                .commandObject(Collections.singletonList(invoiceWrapper))
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
        detail.put("DiscountRate", 0.0);
        detail.put("DiscountAmount", "");
        detail.put("IsDiscount", false);
        detail.put("UserDefineDetails", "");
        detail.put("ItemTypeID", 0);
        return detail;
    }

    /**
     * Khôi phục chính xác logic convertToInvoiceResponse của Huy
     */
    public InvoiceResponse toHubResponse(BkavAdapter.BkavApiResponse response, String clientRequestId) {
        if (response == null) {
            return InvoiceResponse.builder().status(InvoiceResponse.ResponseStatus.FAILED).errorMessage("No response from BKAV").build();
        }

        boolean isSuccess = response.isOk() && !response.isError();
        return InvoiceResponse.builder()
                .clientRequestId(clientRequestId)
                .status(isSuccess ? InvoiceResponse.ResponseStatus.SUCCESS : InvoiceResponse.ResponseStatus.FAILED)
                // FIX: Chuyển int sang String để tránh lỗi Incompatible types
                .errorCode(String.valueOf(response.getStatus()))
                .errorMessage(isSuccess ? null : extractErrorMessage(response))
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
            if (response.getObject() != null) {
                JsonNode node = objectMapper.readTree(objectMapper.writeValueAsString(response.getObject()));
                if (node.isArray() && !node.isEmpty()) {
                    int bkavStatus = node.get(0).path("BkavStatus").asInt();
                    int taxStatus = node.get(0).path("TaxStatus").asInt();

                    switch (bkavStatus) {
                        case 1: case 11: return InvoiceStatus.SIGNING;
                        case 2: return (taxStatus == 33) ? InvoiceStatus.SUCCESS : InvoiceStatus.SENT_TO_PROVIDER;
                        case 3: return InvoiceStatus.CANCELLED;
                        case 6: return InvoiceStatus.REPLACED;
                        case 8: return InvoiceStatus.SUCCESS;
                    }
                }
            }
        } catch (Exception e) { log.error("BKAV status map error", e); }
        return InvoiceStatus.FAILED;
    }

    private String extractField(BkavAdapter.BkavApiResponse response, String field) {
        try {
            JsonNode node = objectMapper.readTree(objectMapper.writeValueAsString(response.getObject()));
            if (node.isArray() && !node.isEmpty()) return node.get(0).path(field).asText();
        } catch (Exception e) { return null; }
        return null;
    }

    private String extractErrorMessage(BkavAdapter.BkavApiResponse response) {
        String msg = extractField(response, "MessLog");
        return msg != null ? msg : String.valueOf(response.getStatus());
    }

    private int getInvoiceTypeId(InvoiceRequest request) {
        if (request.getExtraConfig() != null && request.getExtraConfig().containsKey("InvoiceTypeID"))
            return (Integer) request.getExtraConfig().get("InvoiceTypeID");
        return 1;
    }

    private int mapTaxRateToBkavId(BigDecimal taxRate) {
        if (taxRate == null) return 1;
        double rate = taxRate.doubleValue();
        if (rate == 0) return 1;
        if (rate == 5) return 2;
        if (rate == 10) return 3;
        if (rate == -1) return 4;
        return 3;
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    }
}