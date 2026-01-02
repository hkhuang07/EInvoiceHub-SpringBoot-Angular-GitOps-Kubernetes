package com.einvoicehub.adapter.misa;

import com.einvoicehub.entity.InvoiceMetadata;
import com.einvoicehub.model.request.InvoiceItemRequest;
import com.einvoicehub.model.request.InvoiceRequest;
import com.einvoicehub.model.response.InvoiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MISA API Mapper - Chuyển đổi dữ liệu giữa unified model và MISA API model
 * 
 * Thực hiện mapping từ InvoiceRequest chuẩn hóa sang định dạng MISA MeInvoice
 */
@Component
public class MisaApiMapper {

    private static final Logger logger = LoggerFactory.getLogger(MisaApiMapper.class);
    private static final DateTimeFormatter MISA_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Chuyển đổi InvoiceRequest sang MISA Invoice Request
     */
    public MisaAdapter.MisaInvoiceRequest toMisaRequest(InvoiceRequest request) {
        MisaAdapter.MisaInvoiceRequest misaRequest = new MisaAdapter.MisaInvoiceRequest();
        
        // Invoice code (pattern + serial)
        misaRequest.setInvoiceCode(request.getInvoiceType());
        
        // Invoice name
        misaRequest.setInvoiceName("Hóa đơn giá trị gia tăng");
        
        // Dates
        if (request.getInvoiceDate() != null) {
            misaRequest.setInvoiceDate(request.getInvoiceDate().format(MISA_DATE_FORMAT));
        }
        
        // Currency
        misaRequest.setCurrencyId(request.getCurrency() != null ? request.getCurrency() : "VND");
        misaRequest.setExchangeRate(request.getExchangeRate() != null ? 
            Double.parseDouble(request.getExchangeRate()) : 1.0);
        
        // Sign type (1 = USB token, 2 = HSM with signature display, 3 = HSM async, 4-6 = Post-sign)
        misaRequest.setSignType(1);
        
        // Seller
        misaRequest.setSeller(convertToMisaParty(
            request.getSellerTaxCode(),
            request.getSellerName(),
            request.getSellerTaxCode(),
            request.getSellerAddress(),
            request.getSellerPhone(),
            request.getSellerEmail(),
            request.getSellerBankAccount(),
            request.getSellerBankName()
        ));
        
        // Buyer
        misaRequest.setBuyer(convertToMisaParty(
            request.getBuyerTaxCode(),
            request.getBuyerName(),
            request.getBuyerTaxCode(),
            request.getBuyerAddress(),
            request.getBuyerPhone(),
            request.getBuyerEmail(),
            request.getBuyerBankAccount(),
            request.getBuyerBankName()
        ));
        
        // Items
        if (request.getItems() != null) {
            misaRequest.setItems(request.getItems().stream()
                .map(this::convertToMisaItem)
                .collect(Collectors.toList()));
        }
        
        // Totals
        misaRequest.setTotalAmount(request.getTotalAmount() != null ? 
            request.getTotalAmount().doubleValue() : 0.0);
        misaRequest.setTotalTaxAmount(request.getTotalTaxAmount() != null ? 
            request.getTotalTaxAmount().doubleValue() : 0.0);
        misaRequest.setGrandTotalAmount(request.getGrandTotalAmount() != null ? 
            request.getGrandTotalAmount().doubleValue() : 0.0);
        
        // Payment method
        misaRequest.setPaymentMethod(mapPaymentMethod(request.getPaymentMethod()));
        
        return misaRequest;
    }

    /**
     * Chuyển đổi MISA response sang InvoiceResponse
     */
    public InvoiceResponse toInvoiceResponse(MisaAdapter.MisaApiResponse response, InvoiceMetadata metadata) {
        InvoiceResponse.InvoiceResponseBuilder builder = InvoiceResponse.builder()
            .success(response.isSuccess())
            .providerCode("MISA")
            .clientRequestId(metadata.getClientRequestId())
            .rawResponse(response.toString());

        if (response.isSuccess()) {
            builder.status("ISSUED");
            
            // Parse data JSON
            if (response.getData() != null) {
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> dataMap = new com.fasterxml.jackson.databind.ObjectMapper()
                        .readValue(response.getData(), Map.class);
                    
                    builder.transactionId((String) dataMap.get("invoiceId"))
                        .invoiceNumber((String) dataMap.get("invoiceNumber"))
                        .pdfDownloadUrl((String) dataMap.get("pdfUrl"))
                        .xmlDownloadUrl((String) dataMap.get("xmlUrl"));
                    
                    // Parse signed date
                    Object signedDateObj = dataMap.get("signedDate");
                    if (signedDateObj != null) {
                        try {
                            LocalDateTime signedDate = LocalDateTime.parse(
                                signedDateObj.toString(), MISA_DATE_FORMAT);
                            builder.signedDate(signedDate);
                            builder.issuedDate(signedDate);
                        } catch (Exception e) {
                            logger.warn("Error parsing MISA signed date: {}", signedDateObj);
                        }
                    }
                    
                } catch (Exception e) {
                    logger.warn("Error parsing MISA response data: {}", response.getData());
                }
            }
        } else {
            builder.errorCode(response.getErrorCode())
                .errorMessage(response.getDescriptionErrorCode())
                .status("FAILED");
        }

        return builder.build();
    }

    /**
     * Chuyển đổi status response sang InvoiceResponse
     */
    public InvoiceResponse toStatusResponse(MisaAdapter.MisaStatusResponse response, InvoiceMetadata metadata) {
        return InvoiceResponse.builder()
            .success(response.isSuccess())
            .errorCode(response.getErrorCode())
            .errorMessage(response.getDescriptionErrorCode())
            .providerCode("MISA")
            .transactionId(response.getInvoiceId())
            .invoiceNumber(response.getInvoiceNumber())
            .status(response.getStatus())
            .pdfDownloadUrl(response.getPdfUrl())
            .xmlDownloadUrl(response.getXmlUrl())
            .build();
    }

    /**
     * Chuyển đổi party information
     */
    private MisaAdapter.MisaParty convertToMisaParty(String code, String name, String taxCode,
                                                     String address, String phone, String email,
                                                     String bankAccount, String bankName) {
        MisaAdapter.MisaParty party = new MisaAdapter.MisaParty();
        party.setCode(code);
        party.setName(name);
        party.setTaxCode(taxCode);
        party.setAddress(address);
        party.setPhone(phone);
        party.setEmail(email);
        party.setBankAccount(bankAccount);
        party.setBankName(bankName);
        return party;
    }

    /**
     * Chuyển đổi InvoiceItemRequest sang MisaItem
     */
    private MisaAdapter.MisaItem convertToMisaItem(InvoiceItemRequest item) {
        MisaAdapter.MisaItem misaItem = new MisaAdapter.MisaItem();
        
        misaItem.setItemCode(item.getItemCode());
        misaItem.setItemName(item.getItemName());
        misaItem.setUnitName(item.getUnitName() != null ? item.getUnitName() : "Chiếc");
        misaItem.setQuantity(item.getQuantity() != null ? item.getQuantity().doubleValue() : 1.0);
        misaItem.setUnitPrice(item.getUnitPrice() != null ? item.getUnitPrice().doubleValue() : 0.0);
        misaItem.setAmount(item.getAmount() != null ? item.getAmount().doubleValue() : 0.0);
        
        if (item.getDiscountAmount() != null) {
            misaItem.setDiscountAmount(item.getDiscountAmount().doubleValue());
        } else if (item.getDiscountPercent() != null) {
            misaItem.setDiscountPercent(item.getDiscountPercent().doubleValue());
        }
        
        if (item.getTaxRate() != null) {
            misaItem.setTaxRate(item.getTaxRate().doubleValue());
        }
        
        misaItem.setTaxType(item.getTaxType() != null ? item.getTaxType() : "1");
        
        return misaItem;
    }

    /**
     * Map payment method sang mã MISA
     */
    private String mapPaymentMethod(String paymentMethod) {
        if (paymentMethod == null) {
            return "TM"; // Tiền mặt default
        }
        
        return switch (paymentMethod.toUpperCase()) {
            case "CASH", "TIỀN MẶT", "TM" -> "TM";
            case "BANK", "CHUYỂN KHOẢN", "CK" -> "CK";
            case "CARD", "THẺ" -> "POS";
            default -> "TM";
        };
    }
}