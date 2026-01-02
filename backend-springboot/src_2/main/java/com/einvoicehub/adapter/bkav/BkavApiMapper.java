package com.einvoicehub.adapter.bkav;

import com.einvoicehub.entity.InvoiceMetadata;
import com.einvoicehub.model.request.InvoiceItemRequest;
import com.einvoicehub.model.request.InvoiceRequest;
import com.einvoicehub.model.response.InvoiceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BKAV API Mapper - Chuyển đổi dữ liệu giữa unified model và BKAV API model
 * 
 * Thực hiện mapping từ InvoiceRequest chuẩn hóa sang định dạng BKAV
 * và ngược lại. BKAV sử dụng cấu trúc JSON với CmdType đặc thù.
 */
@Component
public class BkavApiMapper {

    private static final Logger logger = LoggerFactory.getLogger(BkavApiMapper.class);
    private static final DateTimeFormatter BKAV_DATE_FORMAT = DateTimeFormatter.ISO_DATE_TIME;

    private final ObjectMapper objectMapper;

    public BkavApiMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Chuyển đổi InvoiceRequest sang BKAV Command Payload
     */
    public BkavAdapter.BkavCommandPayload toBkavPayload(InvoiceRequest request) {
        BkavAdapter.BkavCommandPayload payload = new BkavAdapter.BkavCommandPayload();
        payload.setCmdType(111); // Create invoice command

        // Invoice data
        BkavAdapter.BkavInvoiceData invoiceData = new BkavAdapter.BkavInvoiceData();
        
        // Invoice type (1 = VAT invoice)
        invoiceData.setInvoiceTypeId(mapInvoiceType(request.getInvoiceType()));
        
        // Invoice date
        if (request.getInvoiceDate() != null) {
            invoiceData.setInvoiceDate(request.getInvoiceDate().format(BKAV_DATE_FORMAT));
        }
        
        // Buyer information
        invoiceData.setBuyerName(request.getBuyerName());
        invoiceData.setBuyerTaxCode(request.getBuyerTaxCode());
        invoiceData.setBuyerUnitName(request.getBuyerName());
        invoiceData.setBuyerAddress(request.getBuyerAddress());
        invoiceData.setBuyerBankAccount(request.getBuyerBankAccount());
        
        // Payment method (3 = Bank transfer)
        invoiceData.setPayMethodId(mapPaymentMethod(request.getPaymentMethod()));
        
        // Receive type (3 = Email + SMS)
        invoiceData.setReceiveTypeId(3);
        
        // Contact info
        invoiceData.setReceiverEmail(request.getBuyerEmail());
        invoiceData.setReceiverMobile(request.getBuyerPhone());
        invoiceData.setReceiverName(request.getBuyerName());
        
        // Delivery address if available
        if (request.getDeliveryAddress() != null) {
            invoiceData.setReceiverAddress(request.getDeliveryAddress());
            invoiceData.setReceiverName(request.getDeliveryName() != null ? 
                request.getDeliveryName() : request.getBuyerName());
        }
        
        // Note and reference
        invoiceData.setNote(request.getInvoiceNote());
        invoiceData.setBillCode(request.getInternalReferenceCode());
        
        // Currency
        invoiceData.setCurrencyId(request.getCurrency() != null ? request.getCurrency() : "VND");
        invoiceData.setExchangeRate(request.getExchangeRate() != null ? 
            Double.parseDouble(request.getExchangeRate()) : 1.0);
        
        // Invoice form and serial
        invoiceData.setInvoiceForm(extractForm(request.getInvoiceType()));
        invoiceData.setInvoiceSerial(extractSerial(request.getInvoiceType()));

        payload.setInvoice(invoiceData);

        // Items
        if (request.getItems() != null) {
            payload.setDetails(request.getItems().stream()
                .map(this::convertToBkavDetail)
                .collect(Collectors.toList()));
        }

        return payload;
    }

    /**
     * Chuyển đổi BKAV result sang InvoiceResponse
     */
    public InvoiceResponse toInvoiceResponse(BkavAdapter.BkavCommandResult result, InvoiceMetadata metadata) {
        InvoiceResponse.InvoiceResponseBuilder builder = InvoiceResponse.builder()
            .success(result.isSuccess())
            .providerCode("BKAV")
            .clientRequestId(metadata.getClientRequestId())
            .transactionId(result.getInvoiceId())
            .invoiceNumber(result.getInvoiceNumber())
            .invoiceSerial(result.getInvoiceSerial())
            .pdfDownloadUrl(result.getPdfUrl())
            .xmlDownloadUrl(result.getXmlUrl())
            .rawResponse(result.toString());

        if (result.isSuccess()) {
            builder.status("ISSUED");
            
            // Parse signed date
            if (result.getSignedDate() != null) {
                try {
                    LocalDateTime signedDate = LocalDateTime.parse(result.getSignedDate(), BKAV_DATE_FORMAT);
                    builder.signedDate(signedDate);
                    builder.issuedDate(signedDate);
                } catch (Exception e) {
                    logger.warn("Error parsing BKAV signed date: {}", result.getSignedDate());
                }
            }
        } else {
            builder.errorCode(result.getErrorCode())
                .errorMessage(result.getErrorMessage())
                .status("FAILED");
        }

        return builder.build();
    }

    /**
     * Parse JSON string thành BkavCommandResult
     */
    public BkavAdapter.BkavCommandResult parseCommandResult(String json) {
        BkavAdapter.BkavCommandResult result = new BkavAdapter.BkavCommandResult();
        
        try {
            // BKAV trả về dạng: {"Success":true,"ErrorCode":"","ErrorMessage":"","Result":{"InvoiceID":"...","InvoiceNumber":"..."}}
            @SuppressWarnings("unchecked")
            Map<String, Object> responseMap = objectMapper.readValue(json, Map.class);
            
            result.setSuccess((Boolean) responseMap.getOrDefault("Success", false));
            result.setErrorCode((String) responseMap.getOrDefault("ErrorCode", ""));
            result.setErrorMessage((String) responseMap.getOrDefault("ErrorMessage", ""));
            
            @SuppressWarnings("unchecked")
            Map<String, Object> resultMap = (Map<String, Object>) responseMap.get("Result");
            if (resultMap != null) {
                result.setInvoiceId((String) resultMap.get("InvoiceID"));
                result.setInvoiceNumber((String) resultMap.get("InvoiceNumber"));
                result.setInvoiceSerial((String) resultMap.get("InvoiceSerial"));
                result.setSignedDate((String) resultMap.get("SignedDate"));
                result.setPdfUrl((String) resultMap.get("PDFLink"));
                result.setXmlUrl((String) resultMap.get("XMLLink"));
            }
            
        } catch (JsonProcessingException e) {
            logger.error("Error parsing BKAV command result", e);
            result.setSuccess(false);
            result.setErrorCode("PARSE_ERROR");
            result.setErrorMessage(e.getMessage());
        }
        
        return result;
    }

    /**
     * Convert object sang JSON string
     */
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Error converting to JSON", e);
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    /**
     * Chuyển đổi InvoiceItemRequest sang BkavInvoiceDetail
     */
    private BkavAdapter.BkavInvoiceDetail convertToBkavDetail(InvoiceItemRequest item) {
        BkavAdapter.BkavInvoiceDetail detail = new BkavAdapter.BkavInvoiceDetail();
        
        detail.setItemName(item.getItemName());
        detail.setUnitName(item.getUnitName() != null ? item.getUnitName() : "Chiếc");
        detail.setQuantity(item.getQuantity() != null ? item.getQuantity().doubleValue() : 1.0);
        detail.setUnitPrice(item.getUnitPrice() != null ? item.getUnitPrice().doubleValue() : 0.0);
        detail.setAmount(item.getAmount() != null ? item.getAmount().doubleValue() : 0.0);
        
        if (item.getDiscountAmount() != null) {
            detail.setDiscountAmount(item.getDiscountAmount().doubleValue());
        }
        
        if (item.getTaxRate() != null) {
            detail.setTaxRate(item.getTaxRate().doubleValue());
        }
        
        detail.setTaxType(item.getTaxType() != null ? item.getTaxType() : "1");
        
        return detail;
    }

    /**
     * Map invoice type sang mã BKAV
     */
    private String mapInvoiceType(String invoiceType) {
        if (invoiceType == null || invoiceType.isBlank()) {
            return "1"; // Default: VAT invoice
        }
        
        return switch (invoiceType.toUpperCase()) {
            case "GTGT", "01GTKT" -> "1";
            case "BANHANG", "02HG" -> "2";
            case "TIENDIEN", "03TCD" -> "3";
            case "BAOHIEM", "04BH" -> "4";
            case "VE", "05VT" -> "5";
            case "KHAC", "06KBN" -> "6";
            default -> "1";
        };
    }

    /**
     * Map payment method sang mã BKAV
     */
    private int mapPaymentMethod(String paymentMethod) {
        if (paymentMethod == null) {
            return 3; // Bank transfer default
        }
        
        return switch (paymentMethod.toUpperCase()) {
            case "CASH", "TIỀN MẶT", "TM" -> 1;
            case "BANK", "CHUYỂN KHOẢN", "CK" -> 3;
            case "CARD", "THẺ" -> 4;
            default -> 3;
        };
    }

    /**
     * Extract form từ invoice type
     */
    private String extractForm(String invoiceType) {
        if (invoiceType == null || invoiceType.isBlank()) {
            return "01";
        }
        
        if (invoiceType.length() >= 2) {
            return invoiceType.substring(0, 2);
        }
        return invoiceType;
    }

    /**
     * Extract serial từ invoice type
     */
    private String extractSerial(String invoiceType) {
        if (invoiceType == null || invoiceType.isBlank()) {
            return "AA";
        }
        
        if (invoiceType.length() > 2) {
            return invoiceType.substring(2);
        }
        return "AA";
    }
}