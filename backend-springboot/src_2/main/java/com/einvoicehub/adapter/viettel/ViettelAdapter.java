package com.einvoicehub.adapter.viettel;

import com.einvoicehub.adapter.InvoiceProvider;
import com.einvoicehub.entity.InvoiceMetadata;
import com.einvoicehub.model.request.InvoiceItemRequest;
import com.einvoicehub.model.request.InvoiceRequest;
import com.einvoicehub.model.response.InvoiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Viettel Adapter - Viettel E-Invoice Provider Integration
 * 
 * Implement tích hợp với API của Viettel
 * Sử dụng REST API với định dạng JSON
 */
@Component("VIETTEL")
public class ViettelAdapter implements InvoiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(ViettelAdapter.class);
    private static final String PROVIDER_CODE = "VIETTEL";
    private static final String PROVIDER_NAME = "Viettel E-Invoice";

    private final WebClient viettelWebClient;
    private final ViettelApiMapper apiMapper;

    public ViettelAdapter(@Qualifier("viettelWebClient") WebClient viettelWebClient,
                         ViettelApiMapper apiMapper) {
        this.viettelWebClient = viettelWebClient;
        this.apiMapper = apiMapper;
    }

    @Override
    public String getProviderCode() {
        return PROVIDER_CODE;
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public InvoiceResponse issueInvoice(InvoiceRequest request, InvoiceMetadata metadata) {
        logger.info("Issuing invoice via Viettel. clientRequestId: {}", metadata.getClientRequestId());

        try {
            // Chuyển đổi request sang định dạng Viettel
            ViettelInvoiceRequest viettelRequest = apiMapper.toViettelRequest(request);

            // Gọi API
            ViettelApiResponse response = viettelWebClient.post()
                .uri("/invoices/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(viettelRequest)
                .retrieve()
                .bodyToMono(ViettelApiResponse.class)
                .block();

            // Xử lý response
            if (response != null && response.isSuccess()) {
                return apiMapper.toInvoiceResponse(response, metadata);
            } else {
                return InvoiceResponse.builder()
                    .success(false)
                    .errorCode(response != null ? response.getErrorCode() : "UNKNOWN")
                    .errorMessage(response != null ? response.getErrorMessage() : "No response from Viettel")
                    .providerCode(PROVIDER_CODE)
                    .clientRequestId(metadata.getClientRequestId())
                    .rawResponse(response != null ? response.toString() : null)
                    .build();
            }

        } catch (Exception e) {
            logger.error("Error issuing invoice via Viettel. clientRequestId: {}", 
                metadata.getClientRequestId(), e);
            
            return InvoiceResponse.builder()
                .success(false)
                .errorCode("VIETTEL_ERROR")
                .errorMessage("Lỗi kết nối Viettel: " + e.getMessage())
                .providerCode(PROVIDER_CODE)
                .clientRequestId(metadata.getClientRequestId())
                .build();
        }
    }

    @Override
    public InvoiceResponse getInvoiceStatus(String providerTransactionId, InvoiceMetadata metadata) {
        logger.info("Getting invoice status from Viettel. transactionId: {}", providerTransactionId);

        try {
            ViettelStatusRequest request = new ViettelStatusRequest();
            request.setTransactionId(providerTransactionId);

            ViettelApiResponse response = viettelWebClient.post()
                .uri("/invoices/status")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ViettelApiResponse.class)
                .block();

            if (response != null && response.isSuccess()) {
                return apiMapper.toInvoiceResponse(response, metadata);
            } else {
                return InvoiceResponse.builder()
                    .success(false)
                    .errorCode(response != null ? response.getErrorCode() : "UNKNOWN")
                    .errorMessage(response != null ? response.getErrorMessage() : "Failed to get status")
                    .providerCode(PROVIDER_CODE)
                    .transactionId(providerTransactionId)
                    .build();
            }

        } catch (Exception e) {
            logger.error("Error getting invoice status from Viettel. transactionId: {}", 
                providerTransactionId, e);
            
            return InvoiceResponse.builder()
                .success(false)
                .errorCode("VIETTEL_STATUS_ERROR")
                .errorMessage("Lỗi lấy trạng thái: " + e.getMessage())
                .providerCode(PROVIDER_CODE)
                .transactionId(providerTransactionId)
                .build();
        }
    }

    @Override
    public InvoiceResponse cancelInvoice(String providerTransactionId, String reason, InvoiceMetadata metadata) {
        logger.info("Cancelling invoice via Viettel. transactionId: {}", providerTransactionId);

        try {
            ViettelCancelRequest request = new ViettelCancelRequest();
            request.setTransactionId(providerTransactionId);
            request.setCancelReason(reason);

            ViettelApiResponse response = viettelWebClient.post()
                .uri("/invoices/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ViettelApiResponse.class)
                .block();

            if (response != null && response.isSuccess()) {
                return InvoiceResponse.builder()
                    .success(true)
                    .providerCode(PROVIDER_CODE)
                    .transactionId(providerTransactionId)
                    .status("CANCELLED")
                    .providerMessage(response.getMessage())
                    .build();
            } else {
                return InvoiceResponse.builder()
                    .success(false)
                    .errorCode(response != null ? response.getErrorCode() : "UNKNOWN")
                    .errorMessage(response != null ? response.getErrorMessage() : "Cancel failed")
                    .providerCode(PROVIDER_CODE)
                    .transactionId(providerTransactionId)
                    .build();
            }

        } catch (Exception e) {
            logger.error("Error cancelling invoice via Viettel. transactionId: {}", 
                providerTransactionId, e);
            
            return InvoiceResponse.builder()
                .success(false)
                .errorCode("VIETTEL_CANCEL_ERROR")
                .errorMessage("Lỗi hủy hóa đơn: " + e.getMessage())
                .providerCode(PROVIDER_CODE)
                .transactionId(providerTransactionId)
                .build();
        }
    }

    @Override
    public InvoiceResponse adjustInvoice(String providerTransactionId, Object adjustmentData, InvoiceMetadata metadata) {
        logger.info("Adjusting invoice via Viettel. transactionId: {}", providerTransactionId);
        
        // Implement adjustment logic
        return InvoiceResponse.builder()
            .success(false)
            .errorCode("NOT_IMPLEMENTED")
            .errorMessage("Adjustment not yet implemented for Viettel")
            .providerCode(PROVIDER_CODE)
            .transactionId(providerTransactionId)
            .build();
    }

    @Override
    public boolean checkConnection() {
        try {
            viettelWebClient.get()
                .uri("/health")
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return true;
        } catch (Exception e) {
            logger.warn("Viettel connection check failed", e);
            return false;
        }
    }

    // ========== Inner Classes for API Mapping ==========

    /**
     * Viettel API Response structure
     */
    static class ViettelApiResponse {
        private boolean success;
        private String errorCode;
        private String errorMessage;
        private String message;
        private ViettelInvoiceData data;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ViettelInvoiceData getData() {
            return data;
        }

        public void setData(ViettelInvoiceData data) {
            this.data = data;
        }
    }

    static class ViettelInvoiceData {
        private String transactionId;
        private String invoiceNumber;
        private String invoiceCode;
        private String pattern;
        private String serial;
        private String status;
        private String signedDate;
        private String pdfUrl;
        private String xmlUrl;
        private String totalAmount;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getInvoiceNumber() {
            return invoiceNumber;
        }

        public void setInvoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
        }

        public String getInvoiceCode() {
            return invoiceCode;
        }

        public void setInvoiceCode(String invoiceCode) {
            this.invoiceCode = invoiceCode;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSignedDate() {
            return signedDate;
        }

        public void setSignedDate(String signedDate) {
            this.signedDate = signedDate;
        }

        public String getPdfUrl() {
            return pdfUrl;
        }

        public void setPdfUrl(String pdfUrl) {
            this.pdfUrl = pdfUrl;
        }

        public String getXmlUrl() {
            return xmlUrl;
        }

        public void setXmlUrl(String xmlUrl) {
            this.xmlUrl = xmlUrl;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }
    }

    static class ViettelInvoiceRequest {
        private String transactionId;
        private String pattern;
        private String serial;
        private String invoiceDate;
        private ViettelParty seller;
        private ViettelParty buyer;
        private List<ViettelItem> items;
        private BigDecimal totalAmount;
        private BigDecimal totalTaxAmount;
        private String currency;
        private String paymentMethod;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public String getInvoiceDate() {
            return invoiceDate;
        }

        public void setInvoiceDate(String invoiceDate) {
            this.invoiceDate = invoiceDate;
        }

        public ViettelParty getSeller() {
            return seller;
        }

        public void setSeller(ViettelParty seller) {
            this.seller = seller;
        }

        public ViettelParty getBuyer() {
            return buyer;
        }

        public void setBuyer(ViettelParty buyer) {
            this.buyer = buyer;
        }

        public List<ViettelItem> getItems() {
            return items;
        }

        public void setItems(List<ViettelItem> items) {
            this.items = items;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public BigDecimal getTotalTaxAmount() {
            return totalTaxAmount;
        }

        public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
            this.totalTaxAmount = totalTaxAmount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }
    }

    static class ViettelParty {
        private String name;
        private String taxCode;
        private String address;
        private String phone;
        private String email;
        private String bankAccount;
        private String bankName;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTaxCode() {
            return taxCode;
        }

        public void setTaxCode(String taxCode) {
            this.taxCode = taxCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBankAccount() {
            return bankAccount;
        }

        public void setBankAccount(String bankAccount) {
            this.bankAccount = bankAccount;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }
    }

    static class ViettelItem {
        private String name;
        private String code;
        private String unit;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount;
        private BigDecimal discount;
        private BigDecimal taxRate;
        private String taxType;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }

        public BigDecimal getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(BigDecimal taxRate) {
            this.taxRate = taxRate;
        }

        public String getTaxType() {
            return taxType;
        }

        public void setTaxType(String taxType) {
            this.taxType = taxType;
        }
    }

    static class ViettelStatusRequest {
        private String transactionId;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }
    }

    static class ViettelCancelRequest {
        private String transactionId;
        private String cancelReason;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getCancelReason() {
            return cancelReason;
        }

        public void setCancelReason(String cancelReason) {
            this.cancelReason = cancelReason;
        }
    }
}