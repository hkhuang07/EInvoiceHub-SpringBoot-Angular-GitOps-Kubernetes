package com.einvoicehub.adapter.viettel;

import com.einvoicehub.entity.InvoiceMetadata;
import com.einvoicehub.model.request.InvoiceItemRequest;
import com.einvoicehub.model.request.InvoiceRequest;
import com.einvoicehub.model.response.InvoiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Viettel API Mapper - Chuyển đổi dữ liệu giữa unified model và Viettel API model
 * 
 * Thực hiện mapping từ InvoiceRequest chuẩn hóa sang định dạng Viettel
 * và ngược lại
 */
@Component
public class ViettelApiMapper {

    private static final Logger logger = LoggerFactory.getLogger(ViettelApiMapper.class);
    private static final DateTimeFormatter VIETTEL_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Chuyển đổi InvoiceRequest sang ViettelInvoiceRequest
     */
    public ViettelAdapter.ViettelInvoiceRequest toViettelRequest(InvoiceRequest request) {
        ViettelAdapter.ViettelInvoiceRequest viettelRequest = new ViettelAdapter.ViettelInvoiceRequest();
        
        // Transaction ID
        viettelRequest.setTransactionId(request.getInternalReferenceCode());
        
        // Invoice pattern và serial (lấy từ request hoặc config)
        viettelRequest.setPattern(extractPattern(request.getInvoiceType()));
        viettelRequest.setSerial(extractSerial(request.getInvoiceType()));
        
        // Invoice date
        if (request.getInvoiceDate() != null) {
            viettelRequest.setInvoiceDate(request.getInvoiceDate().format(VIETTEL_DATE_FORMAT));
        }
        
        // Seller
        viettelRequest.setSeller(convertToViettelParty(
            request.getSellerName(),
            request.getSellerTaxCode(),
            request.getSellerAddress(),
            request.getSellerPhone(),
            request.getSellerEmail(),
            request.getSellerBankAccount(),
            request.getSellerBankName()
        ));
        
        // Buyer
        viettelRequest.setBuyer(convertToViettelParty(
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
            viettelRequest.setItems(request.getItems().stream()
                .map(this::convertToViettelItem)
                .collect(Collectors.toList()));
        }
        
        // Totals
        viettelRequest.setTotalAmount(request.getGrandTotalAmount() != null ? 
            request.getGrandTotalAmount() : BigDecimal.ZERO);
        viettelRequest.setTotalTaxAmount(request.getTotalTaxAmount() != null ? 
            request.getTotalTaxAmount() : BigDecimal.ZERO);
        viettelRequest.setCurrency(request.getCurrency() != null ? request.getCurrency() : "VND");
        
        // Payment method
        viettelRequest.setPaymentMethod(mapPaymentMethod(request.getPaymentMethod()));
        
        return viettelRequest;
    }

    /**
     * Chuyển đổi Viettel response sang InvoiceResponse chuẩn
     */
    public InvoiceResponse toInvoiceResponse(ViettelAdapter.ViettelApiResponse viettelResponse, 
                                             InvoiceMetadata metadata) {
        ViettelAdapter.ViettelInvoiceData data = viettelResponse.getData();
        
        InvoiceResponse.InvoiceResponseBuilder builder = InvoiceResponse.builder()
            .success(viettelResponse.isSuccess())
            .providerCode("VIETTEL")
            .clientRequestId(metadata.getClientRequestId())
            .rawResponse(viettelResponse.toString());

        if (viettelResponse.isSuccess() && data != null) {
            builder.transactionId(data.getTransactionId())
                .invoiceNumber(data.getInvoiceNumber())
                .invoiceCode(data.getInvoiceCode())
                .invoicePattern(data.getPattern())
                .invoiceSerial(data.getSerial())
                .status(data.getStatus())
                .pdfDownloadUrl(data.getPdfUrl())
                .xmlDownloadUrl(data.getXmlUrl());

            // Parse signed date
            if (data.getSignedDate() != null) {
                try {
                    LocalDateTime signedDate = LocalDateTime.parse(data.getSignedDate(), VIETTEL_DATE_FORMAT);
                    builder.signedDate(signedDate);
                } catch (Exception e) {
                    logger.warn("Error parsing Viettel signed date: {}", data.getSignedDate());
                }
            }

            // Parse total amount
            if (data.getTotalAmount() != null) {
                try {
                    builder.grandTotalAmount(new BigDecimal(data.getTotalAmount()));
                } catch (NumberFormatException e) {
                    logger.warn("Error parsing Viettel total amount: {}", data.getTotalAmount());
                }
            }
        } else {
            builder.errorCode(viettelResponse.getErrorCode())
                .errorMessage(viettelResponse.getErrorMessage());
        }

        return builder.build();
    }

    /**
     * Chuyển đổi party information
     */
    private ViettelAdapter.ViettelParty convertToViettelParty(String name, String taxCode,
                                                              String address, String phone,
                                                              String email, String bankAccount,
                                                              String bankName) {
        ViettelAdapter.ViettelParty party = new ViettelAdapter.ViettelParty();
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
     * Chuyển đổi InvoiceItemRequest sang ViettelItem
     */
    public ViettelAdapter.ViettelItem convertToViettelItem(InvoiceItemRequest item) {
        ViettelAdapter.ViettelItem viettelItem = new ViettelAdapter.ViettelItem();
        
        viettelItem.setName(item.getItemName());
        viettelItem.setCode(item.getItemCode());
        viettelItem.setUnit(item.getUnitName());
        viettelItem.setQuantity(item.getQuantity());
        viettelItem.setUnitPrice(item.getUnitPrice());
        viettelItem.setAmount(item.getAmount());
        
        // Discount
        if (item.getDiscountAmount() != null) {
            viettelItem.setDiscount(item.getDiscountAmount());
        } else if (item.getDiscountPercent() != null) {
            // Tính discount amount từ phần trăm
            if (item.getAmount() != null) {
                viettelItem.setDiscount(item.getAmount()
                    .multiply(item.getDiscountPercent())
                    .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP));
            }
        }
        
        // Tax
        viettelItem.setTaxRate(item.getTaxRate());
        viettelItem.setTaxType(item.getTaxType());
        
        return viettelItem;
    }

    /**
     * Extract pattern từ invoice type
     * Ví dụ: "01GTKT" -> "01"
     */
    private String extractPattern(String invoiceType) {
        if (invoiceType == null || invoiceType.isBlank()) {
            return "01"; // Default pattern for VAT invoice
        }
        
        // Lấy 2 ký tự đầu
        if (invoiceType.length() >= 2) {
            return invoiceType.substring(0, 2);
        }
        return invoiceType;
    }

    /**
     * Extract serial từ invoice type
     * Ví dụ: "01GTKT" -> "GTKT"
     */
    private String extractSerial(String invoiceType) {
        if (invoiceType == null || invoiceType.isBlank()) {
            return "GTKT"; // Default serial for VAT invoice
        }
        
        // Lấy phần còn lại sau 2 ký tự đầu
        if (invoiceType.length() > 2) {
            return invoiceType.substring(2);
        }
        return invoiceType;
    }

    /**
     * Map payment method sang mã Viettel
     */
    private String mapPaymentMethod(String paymentMethod) {
        if (paymentMethod == null) {
            return "TM"; // Tiền mặt default
        }
        
        return switch (paymentMethod.toUpperCase()) {
            case "CASH", "TIỀN MẶT", "TM" -> "TM";
            case "BANK", "CHUYỂN KHOẢN", "CK" -> "CK";
            case "CARD", "THẺ", "VISA", "MASTER" -> "POS";
            case "EWALLET", "Ví ĐIỆN TỬ" -> "EWT";
            default -> "TM";
        };
    }
}