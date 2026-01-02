package com.einvoicehub.adapter.misa;

import com.einvoicehub.adapter.InvoiceProvider;
import com.einvoicehub.entity.InvoiceMetadata;
import com.einvoicehub.model.request.InvoiceItemRequest;
import com.einvoicehub.model.request.InvoiceRequest;
import com.einvoicehub.model.response.InvoiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * MISA Adapter - MISA MeInvoice Provider Integration
 * 
 * Implement tích hợp với API của MISA MeInvoice
 * Sử dụng REST API với OAuth 2.0 Bearer Token
 */
@Component("MISA")
public class MisaAdapter implements InvoiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(MisaAdapter.class);
    private static final String PROVIDER_CODE = "MISA";
    private static final String PROVIDER_NAME = "MISA MeInvoice";
    private static final DateTimeFormatter MISA_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final WebClient misaWebClient;
    private final MisaApiMapper apiMapper;
    private final MisaTokenManager tokenManager;
    private final String appId;

    // Cache cho token
    private final Map<Long, String> tokenCache = new ConcurrentHashMap<>();

    public MisaAdapter(@Qualifier("misaWebClient") WebClient misaWebClient,
                      MisaApiMapper apiMapper,
                      MisaTokenManager tokenManager,
                      @Value("${providers.misa.app-id}") String appId) {
        this.misaWebClient = misaWebClient;
        this.apiMapper = apiMapper;
        this.tokenManager = tokenManager;
        this.appId = appId;
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
        logger.info("Issuing invoice via MISA. clientRequestId: {}", metadata.getClientRequestId());

        try {
            // Bước 1: Lấy OAuth token
            String token = tokenManager.getValidToken();
            
            // Bước 2: Chuyển đổi request sang định dạng MISA
            MisaInvoiceRequest misaRequest = apiMapper.toMisaRequest(request);

            // Bước 3: Gọi API với Bearer token
            MisaApiResponse response = misaWebClient.post()
                .uri("/invoice")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(misaRequest)
                .retrieve()
                .bodyToMono(MisaApiResponse.class)
                .onErrorResume(e -> {
                    if (e instanceof WebClientResponseException.Unauthorized) {
                        // Token hết hạn, lấy token mới và thử lại
                        logger.warn("MISA token expired, refreshing...");
                        tokenManager.refreshToken();
                        String newToken = tokenManager.getValidToken();
                        
                        return misaWebClient.post()
                            .uri("/invoice")
                            .header("Authorization", "Bearer " + newToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(misaRequest)
                            .retrieve()
                            .bodyToMono(MisaApiResponse.class);
                    }
                    return Mono.error(e);
                })
                .block();

            // Bước 4: Xử lý response
            if (response != null && response.isSuccess()) {
                return apiMapper.toInvoiceResponse(response, metadata);
            } else {
                return InvoiceResponse.builder()
                    .success(false)
                    .errorCode(response != null ? response.getErrorCode() : "UNKNOWN")
                    .errorMessage(response != null ? response.getDescriptionErrorCode() : "No response from MISA")
                    .providerCode(PROVIDER_CODE)
                    .clientRequestId(metadata.getClientRequestId())
                    .rawResponse(response != null ? response.toString() : null)
                    .build();
            }

        } catch (Exception e) {
            logger.error("Error issuing invoice via MISA. clientRequestId: {}", 
                metadata.getClientRequestId(), e);
            
            return InvoiceResponse.builder()
                .success(false)
                .errorCode("MISA_ERROR")
                .errorMessage("Lỗi kết nối MISA: " + e.getMessage())
                .providerCode(PROVIDER_CODE)
                .clientRequestId(metadata.getClientRequestId())
                .build();
        }
    }

    @Override
    public InvoiceResponse getInvoiceStatus(String providerTransactionId, InvoiceMetadata metadata) {
        logger.info("Getting invoice status from MISA. transactionId: {}", providerTransactionId);

        try {
            String token = tokenManager.getValidToken();

            // Gọi API lấy trạng thái
            MisaStatusResponse response = misaWebClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/invoice/status")
                    .queryParam("invoiceId", providerTransactionId)
                    .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(MisaStatusResponse.class)
                .block();

            if (response != null && response.isSuccess()) {
                return apiMapper.toStatusResponse(response, metadata);
            } else {
                return InvoiceResponse.builder()
                    .success(false)
                    .errorCode(response != null ? response.getErrorCode() : "UNKNOWN")
                    .errorMessage(response != null ? response.getDescriptionErrorCode() : "Failed to get status")
                    .providerCode(PROVIDER_CODE)
                    .transactionId(providerTransactionId)
                    .build();
            }

        } catch (Exception e) {
            logger.error("Error getting invoice status from MISA. transactionId: {}", 
                providerTransactionId, e);
            
            return InvoiceResponse.builder()
                .success(false)
                .errorCode("MISA_STATUS_ERROR")
                .errorMessage("Lỗi lấy trạng thái: " + e.getMessage())
                .providerCode(PROVIDER_CODE)
                .transactionId(providerTransactionId)
                .build();
        }
    }

    @Override
    public InvoiceResponse cancelInvoice(String providerTransactionId, String reason, InvoiceMetadata metadata) {
        logger.info("Cancelling invoice via MISA. transactionId: {}", providerTransactionId);

        try {
            String token = tokenManager.getValidToken();

            MisaCancelRequest request = new MisaCancelRequest();
            request.setInvoiceId(providerTransactionId);
            request.setReason(reason);

            MisaApiResponse response = misaWebClient.post()
                .uri("/invoice/cancel")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(MisaApiResponse.class)
                .block();

            if (response != null && response.isSuccess()) {
                return InvoiceResponse.builder()
                    .success(true)
                    .providerCode(PROVIDER_CODE)
                    .transactionId(providerTransactionId)
                    .status("CANCELLED")
                    .providerMessage(response.getDescriptionErrorCode())
                    .build();
            } else {
                return InvoiceResponse.builder()
                    .success(false)
                    .errorCode(response != null ? response.getErrorCode() : "UNKNOWN")
                    .errorMessage(response != null ? response.getDescriptionErrorCode() : "Cancel failed")
                    .providerCode(PROVIDER_CODE)
                    .transactionId(providerTransactionId)
                    .build();
            }

        } catch (Exception e) {
            logger.error("Error cancelling invoice via MISA. transactionId: {}", 
                providerTransactionId, e);
            
            return InvoiceResponse.builder()
                .success(false)
                .errorCode("MISA_CANCEL_ERROR")
                .errorMessage("Lỗi hủy hóa đơn: " + e.getMessage())
                .providerCode(PROVIDER_CODE)
                .transactionId(providerTransactionId)
                .build();
        }
    }

    @Override
    public InvoiceResponse adjustInvoice(String providerTransactionId, Object adjustmentData, InvoiceMetadata metadata) {
        logger.info("Adjusting invoice via MISA. transactionId: {}", providerTransactionId);
        
        return InvoiceResponse.builder()
            .success(false)
            .errorCode("NOT_IMPLEMENTED")
            .errorMessage("Adjustment not yet implemented for MISA")
            .providerCode(PROVIDER_CODE)
            .transactionId(providerTransactionId)
            .build();
    }

    @Override
    public boolean checkConnection() {
        try {
            // Kiểm tra kết nối bằng cách lấy token
            return tokenManager.getValidToken() != null;
        } catch (Exception e) {
            logger.warn("MISA connection check failed", e);
            return false;
        }
    }

    // ========== Inner Classes ==========

    static class MisaApiResponse {
        private boolean success;
        private String errorCode;
        private String descriptionErrorCode;
        private String createInvoiceResult;
        private String publishInvoiceResult;
        private String data;
        private String customData;

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

        public String getDescriptionErrorCode() {
            return descriptionErrorCode;
        }

        public void setDescriptionErrorCode(String descriptionErrorCode) {
            this.descriptionErrorCode = descriptionErrorCode;
        }

        public String getCreateInvoiceResult() {
            return createInvoiceResult;
        }

        public void setCreateInvoiceResult(String createInvoiceResult) {
            this.createInvoiceResult = createInvoiceResult;
        }

        public String getPublishInvoiceResult() {
            return publishInvoiceResult;
        }

        public void setPublishInvoiceResult(String publishInvoiceResult) {
            this.publishInvoiceResult = publishInvoiceResult;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getCustomData() {
            return customData;
        }

        public void setCustomData(String customData) {
            this.customData = customData;
        }
    }

    static class MisaInvoiceRequest {
        private String invoiceCode;
        private String invoiceName;
        private String invoiceDate;
        private String signedDate;
        private String currencyId;
        private double exchangeRate;
        private int signType;
        private MisaParty seller;
        private MisaParty buyer;
        private List<MisaItem> items;
        private double totalAmount;
        private double totalTaxAmount;
        private double grandTotalAmount;
        private String paymentMethod;

        // Getters and Setters
        public String getInvoiceCode() {
            return invoiceCode;
        }

        public void setInvoiceCode(String invoiceCode) {
            this.invoiceCode = invoiceCode;
        }

        public String getInvoiceName() {
            return invoiceName;
        }

        public void setInvoiceName(String invoiceName) {
            this.invoiceName = invoiceName;
        }

        public String getInvoiceDate() {
            return invoiceDate;
        }

        public void setInvoiceDate(String invoiceDate) {
            this.invoiceDate = invoiceDate;
        }

        public String getSignedDate() {
            return signedDate;
        }

        public void setSignedDate(String signedDate) {
            this.signedDate = signedDate;
        }

        public String getCurrencyId() {
            return currencyId;
        }

        public void setCurrencyId(String currencyId) {
            this.currencyId = currencyId;
        }

        public double getExchangeRate() {
            return exchangeRate;
        }

        public void setExchangeRate(double exchangeRate) {
            this.exchangeRate = exchangeRate;
        }

        public int getSignType() {
            return signType;
        }

        public void setSignType(int signType) {
            this.signType = signType;
        }

        public MisaParty getSeller() {
            return seller;
        }

        public void setSeller(MisaParty seller) {
            this.seller = seller;
        }

        public MisaParty getBuyer() {
            return buyer;
        }

        public void setBuyer(MisaParty buyer) {
            this.buyer = buyer;
        }

        public List<MisaItem> getItems() {
            return items;
        }

        public void setItems(List<MisaItem> items) {
            this.items = items;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public double getTotalTaxAmount() {
            return totalTaxAmount;
        }

        public void setTotalTaxAmount(double totalTaxAmount) {
            this.totalTaxAmount = totalTaxAmount;
        }

        public double getGrandTotalAmount() {
            return grandTotalAmount;
        }

        public void setGrandTotalAmount(double grandTotalAmount) {
            this.grandTotalAmount = grandTotalAmount;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }
    }

    static class MisaParty {
        private String code;
        private String name;
        private String taxCode;
        private String address;
        private String phone;
        private String email;
        private String bankAccount;
        private String bankName;

        // Getters and Setters
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

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

    static class MisaItem {
        private String itemCode;
        private String itemName;
        private String unitName;
        private double quantity;
        private double unitPrice;
        private double amount;
        private double discountAmount;
        private double discountPercent;
        private double taxRate;
        private String taxType;
        private String taxCategory;

        // Getters and Setters
        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(double discountAmount) {
            this.discountAmount = discountAmount;
        }

        public double getDiscountPercent() {
            return discountPercent;
        }

        public void setDiscountPercent(double discountPercent) {
            this.discountPercent = discountPercent;
        }

        public double getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(double taxRate) {
            this.taxRate = taxRate;
        }

        public String getTaxType() {
            return taxType;
        }

        public void setTaxType(String taxType) {
            this.taxType = taxType;
        }

        public String getTaxCategory() {
            return taxCategory;
        }

        public void setTaxCategory(String taxCategory) {
            this.taxCategory = taxCategory;
        }
    }

    static class MisaStatusResponse {
        private boolean success;
        private String errorCode;
        private String descriptionErrorCode;
        private String invoiceId;
        private String invoiceNumber;
        private String status;
        private String signedDate;
        private String pdfUrl;
        private String xmlUrl;

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

        public String getDescriptionErrorCode() {
            return descriptionErrorCode;
        }

        public void setDescriptionErrorCode(String descriptionErrorCode) {
            this.descriptionErrorCode = descriptionErrorCode;
        }

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getInvoiceNumber() {
            return invoiceNumber;
        }

        public void setInvoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
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
    }

    static class MisaCancelRequest {
        private String invoiceId;
        private String reason;

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}