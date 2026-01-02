package com.einvoicehub.adapter.bkav;

import com.einvoicehub.adapter.InvoiceProvider;
import com.einvoicehub.entity.InvoiceMetadata;
import com.einvoicehub.model.request.InvoiceItemRequest;
import com.einvoicehub.model.request.InvoiceRequest;
import com.einvoicehub.model.response.InvoiceResponse;
import com.einvoicehub.util.AesEncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * BKAV Adapter - BKAV eInvoice Provider Integration
 * 
 * Implement tích hợp với API của BKAV
 * Sử dụng WebService với mã hóa AES-256 cho payload
 */
@Component("BKAV")
public class BkavAdapter implements InvoiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(BkavAdapter.class);
    private static final String PROVIDER_CODE = "BKAV";
    private static final String PROVIDER_NAME = "BKAV eInvoice";
    private static final DateTimeFormatter BKAV_DATE_FORMAT = DateTimeFormatter.ISO_DATE_TIME;

    private final WebClient bkavWebClient;
    private final BkavApiMapper apiMapper;
    private final AesEncryptionUtil encryptionUtil;
    private final String encryptionKey;

    public BkavAdapter(@Qualifier("bkavWebClient") WebClient bkavWebClient,
                      BkavApiMapper apiMapper,
                      AesEncryptionUtil encryptionUtil,
                      @Value("${providers.bkav.encryption-key}") String encryptionKey) {
        this.bkavWebClient = bkavWebClient;
        this.apiMapper = apiMapper;
        this.encryptionUtil = encryptionUtil;
        this.encryptionKey = encryptionKey;
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
        logger.info("Issuing invoice via BKAV. clientRequestId: {}", metadata.getClientRequestId());

        try {
            // Bước 1: Chuyển đổi request sang định dạng BKAV
            BkavCommandPayload payload = apiMapper.toBkavPayload(request);
            
            // Bước 2: Mã hóa payload bằng AES-256
            String encryptedData = encryptionUtil.encrypt(
                apiMapper.toJson(payload), 
                encryptionKey
            );
            
            // Bước 3: Tạo request envelope
            BkavSoapRequest soapRequest = new BkavSoapRequest();
            soapRequest.setEncryptedData(encryptedData);
            soapRequest.setCmdType(payload.getCmdType());

            // Bước 4: Gọi WebService
            String responseXml = bkavWebClient.post()
                .uri("/ws/invoice.asmx")
                .contentType(MediaType.TEXT_XML)
                .bodyValue(soapRequest.toSoapXml())
                .retrieve()
                .bodyToMono(String.class)
                .block();

            // Bước 5: Giải mã và xử lý response
            BkavSoapResponse soapResponse = parseSoapResponse(responseXml);
            
            if (soapResponse.isSuccess()) {
                String decryptedResponse = encryptionUtil.decrypt(
                    soapResponse.getEncryptedData(), 
                    encryptionKey
                );
                BkavCommandResult result = apiMapper.parseCommandResult(decryptedResponse);
                
                return apiMapper.toInvoiceResponse(result, metadata);
            } else {
                return InvoiceResponse.builder()
                    .success(false)
                    .errorCode(soapResponse.getErrorCode())
                    .errorMessage(soapResponse.getErrorMessage())
                    .providerCode(PROVIDER_CODE)
                    .clientRequestId(metadata.getClientRequestId())
                    .rawResponse(responseXml)
                    .build();
            }

        } catch (Exception e) {
            logger.error("Error issuing invoice via BKAV. clientRequestId: {}", 
                metadata.getClientRequestId(), e);
            
            return InvoiceResponse.builder()
                .success(false)
                .errorCode("BKAV_ERROR")
                .errorMessage("Lỗi kết nối BKAV: " + e.getMessage())
                .providerCode(PROVIDER_CODE)
                .clientRequestId(metadata.getClientRequestId())
                .build();
        }
    }

    @Override
    public InvoiceResponse getInvoiceStatus(String providerTransactionId, InvoiceMetadata metadata) {
        logger.info("Getting invoice status from BKAV. transactionId: {}", providerTransactionId);

        try {
            // Tạo payload truy vấn trạng thái (CmdType = 112)
            BkavCommandPayload payload = new BkavCommandPayload();
            payload.setCmdType(112); // Query status
            payload.setInvoiceId(providerTransactionId);

            // Mã hóa và gọi API tương tự như issueInvoice
            String encryptedData = encryptionUtil.encrypt(
                apiMapper.toJson(payload), 
                encryptionKey
            );

            String responseXml = bkavWebClient.post()
                .uri("/ws/invoice.asmx")
                .contentType(MediaType.TEXT_XML)
                .bodyValue(createSoapEnvelope(encryptedData, 112))
                .retrieve()
                .bodyToMono(String.class)
                .block();

            // Parse và xử lý response
            BkavSoapResponse soapResponse = parseSoapResponse(responseXml);
            
            if (soapResponse.isSuccess()) {
                String decryptedResponse = encryptionUtil.decrypt(
                    soapResponse.getEncryptedData(), 
                    encryptionKey
                );
                BkavCommandResult result = apiMapper.parseCommandResult(decryptedResponse);
                
                return apiMapper.toInvoiceResponse(result, metadata);
            } else {
                return InvoiceResponse.builder()
                    .success(false)
                    .errorCode(soapResponse.getErrorCode())
                    .errorMessage(soapResponse.getErrorMessage())
                    .providerCode(PROVIDER_CODE)
                    .transactionId(providerTransactionId)
                    .build();
            }

        } catch (Exception e) {
            logger.error("Error getting invoice status from BKAV. transactionId: {}", 
                providerTransactionId, e);
            
            return InvoiceResponse.builder()
                .success(false)
                .errorCode("BKAV_STATUS_ERROR")
                .errorMessage("Lỗi lấy trạng thái: " + e.getMessage())
                .providerCode(PROVIDER_CODE)
                .transactionId(providerTransactionId)
                .build();
        }
    }

    @Override
    public InvoiceResponse cancelInvoice(String providerTransactionId, String reason, InvoiceMetadata metadata) {
        logger.info("Cancelling invoice via BKAV. transactionId: {}", providerTransactionId);

        try {
            // CmdType = 114 cho hủy hóa đơn
            BkavCommandPayload payload = new BkavCommandPayload();
            payload.setCmdType(114); // Cancel invoice
            payload.setInvoiceId(providerTransactionId);
            payload.setNote(reason);

            String encryptedData = encryptionUtil.encrypt(
                apiMapper.toJson(payload), 
                encryptionKey
            );

            String responseXml = bkavWebClient.post()
                .uri("/ws/invoice.asmx")
                .contentType(MediaType.TEXT_XML)
                .bodyValue(createSoapEnvelope(encryptedData, 114))
                .retrieve()
                .bodyToMono(String.class)
                .block();

            BkavSoapResponse soapResponse = parseSoapResponse(responseXml);
            
            if (soapResponse.isSuccess()) {
                return InvoiceResponse.builder()
                    .success(true)
                    .providerCode(PROVIDER_CODE)
                    .transactionId(providerTransactionId)
                    .status("CANCELLED")
                    .build();
            } else {
                return InvoiceResponse.builder()
                    .success(false)
                    .errorCode(soapResponse.getErrorCode())
                    .errorMessage(soapResponse.getErrorMessage())
                    .providerCode(PROVIDER_CODE)
                    .transactionId(providerTransactionId)
                    .build();
            }

        } catch (Exception e) {
            logger.error("Error cancelling invoice via BKAV. transactionId: {}", 
                providerTransactionId, e);
            
            return InvoiceResponse.builder()
                .success(false)
                .errorCode("BKAV_CANCEL_ERROR")
                .errorMessage("Lỗi hủy hóa đơn: " + e.getMessage())
                .providerCode(PROVIDER_CODE)
                .transactionId(providerTransactionId)
                .build();
        }
    }

    @Override
    public InvoiceResponse adjustInvoice(String providerTransactionId, Object adjustmentData, InvoiceMetadata metadata) {
        logger.info("Adjusting invoice via BKAV. transactionId: {}", providerTransactionId);
        
        return InvoiceResponse.builder()
            .success(false)
            .errorCode("NOT_IMPLEMENTED")
            .errorMessage("Adjustment not yet implemented for BKAV")
            .providerCode(PROVIDER_CODE)
            .transactionId(providerTransactionId)
            .build();
    }

    @Override
    public boolean checkConnection() {
        try {
            // Gọi endpoint health check
            String response = bkavWebClient.get()
                .uri("/ws/invoice.asmx?wsdl")
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return response != null && response.contains("InvoiceService");
        } catch (Exception e) {
            logger.warn("BKAV connection check failed", e);
            return false;
        }
    }

    /**
     * Tạo SOAP envelope cho request
     */
    private String createSoapEnvelope(String encryptedData, int cmdType) {
        return String.format("""
            <?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                <soap:Body>
                    <ExecuteCommand xmlns="http://tempuri.org/">
                        <encryptedData>%s</encryptedData>
                        <cmdType>%d</cmdType>
                    </ExecuteCommand>
                </soap:Body>
            </soap:Envelope>
            """, encryptedData, cmdType);
    }

    /**
     * Parse SOAP response và trích xuất dữ liệu mã hóa
     */
    private BkavSoapResponse parseSoapResponse(String responseXml) {
        BkavSoapResponse response = new BkavSoapResponse();
        
        try {
            // Trích xuất encrypted data từ response
            String encryptedDataStart = "<EncryptedData>";
            String encryptedDataEnd = "</EncryptedData>";
            
            int start = responseXml.indexOf(encryptedDataStart);
            int end = responseXml.indexOf(encryptedDataEnd);
            
            if (start >= 0 && end >= 0) {
                String encryptedData = responseXml.substring(
                    start + encryptedDataStart.length(), end
                ).trim();
                response.setEncryptedData(encryptedData);
                response.setSuccess(true);
            } else {
                // Parse error
                String errorStart = "<ErrorCode>";
                String errorEnd = "</ErrorCode>";
                
                int errStart = responseXml.indexOf(errorStart);
                int errEnd = responseXml.indexOf(errorEnd);
                
                if (errStart >= 0 && errEnd >= 0) {
                    response.setErrorCode(responseXml.substring(
                        errStart + errorStart.length(), errEnd
                    ).trim());
                }
                
                response.setSuccess(false);
            }
            
        } catch (Exception e) {
            logger.error("Error parsing BKAV SOAP response", e);
            response.setSuccess(false);
            response.setErrorCode("PARSE_ERROR");
            response.setErrorMessage(e.getMessage());
        }
        
        return response;
    }

    // ========== Inner Classes ==========

    static class BkavSoapRequest {
        private String encryptedData;
        private int cmdType;

        public String getEncryptedData() {
            return encryptedData;
        }

        public void setEncryptedData(String encryptedData) {
            this.encryptedData = encryptedData;
        }

        public int getCmdType() {
            return cmdType;
        }

        public void setCmdType(int cmdType) {
            this.cmdType = cmdType;
        }

        public String toSoapXml() {
            return String.format("""
                <?xml version="1.0" encoding="utf-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                    <soap:Body>
                        <ExecuteCommand xmlns="http://tempuri.org/">
                            <encryptedData>%s</encryptedData>
                            <cmdType>%d</cmdType>
                        </ExecuteCommand>
                    </soap:Body>
                </soap:Envelope>
                """, encryptedData, cmdType);
        }
    }

    static class BkavSoapResponse {
        private boolean success;
        private String encryptedData;
        private String errorCode;
        private String errorMessage;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getEncryptedData() {
            return encryptedData;
        }

        public void setEncryptedData(String encryptedData) {
            this.encryptedData = encryptedData;
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
    }

    static class BkavCommandPayload {
        private int cmdType; // 111: create, 112: status, 113: adjust, 114: cancel
        private String invoiceId;
        private String note;
        private BkavInvoiceData invoice;
        private List<BkavInvoiceDetail> details;

        public int getCmdType() {
            return cmdType;
        }

        public void setCmdType(int cmdType) {
            this.cmdType = cmdType;
        }

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public BkavInvoiceData getInvoice() {
            return invoice;
        }

        public void setInvoice(BkavInvoiceData invoice) {
            this.invoice = invoice;
        }

        public List<BkavInvoiceDetail> getDetails() {
            return details;
        }

        public void setDetails(List<BkavInvoiceDetail> details) {
            this.details = details;
        }
    }

    static class BkavInvoiceData {
        private String invoiceTypeId;
        private String invoiceDate;
        private String buyerName;
        private String buyerTaxCode;
        private String buyerUnitName;
        private String buyerAddress;
        private String buyerBankAccount;
        private int payMethodId;
        private int receiveTypeId;
        private String receiverEmail;
        private String receiverMobile;
        private String receiverAddress;
        private String receiverName;
        private String note;
        private String billCode;
        private String currencyId;
        private double exchangeRate;
        private String invoiceForm;
        private String invoiceSerial;

        // Getters and Setters
        public String getInvoiceTypeId() {
            return invoiceTypeId;
        }

        public void setInvoiceTypeId(String invoiceTypeId) {
            this.invoiceTypeId = invoiceTypeId;
        }

        public String getInvoiceDate() {
            return invoiceDate;
        }

        public void setInvoiceDate(String invoiceDate) {
            this.invoiceDate = invoiceDate;
        }

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
        }

        public String getBuyerTaxCode() {
            return buyerTaxCode;
        }

        public void setBuyerTaxCode(String buyerTaxCode) {
            this.buyerTaxCode = buyerTaxCode;
        }

        public String getBuyerUnitName() {
            return buyerUnitName;
        }

        public void setBuyerUnitName(String buyerUnitName) {
            this.buyerUnitName = buyerUnitName;
        }

        public String getBuyerAddress() {
            return buyerAddress;
        }

        public void setBuyerAddress(String buyerAddress) {
            this.buyerAddress = buyerAddress;
        }

        public String getBuyerBankAccount() {
            return buyerBankAccount;
        }

        public void setBuyerBankAccount(String buyerBankAccount) {
            this.buyerBankAccount = buyerBankAccount;
        }

        public int getPayMethodId() {
            return payMethodId;
        }

        public void setPayMethodId(int payMethodId) {
            this.payMethodId = payMethodId;
        }

        public int getReceiveTypeId() {
            return receiveTypeId;
        }

        public void setReceiveTypeId(int receiveTypeId) {
            this.receiveTypeId = receiveTypeId;
        }

        public String getReceiverEmail() {
            return receiverEmail;
        }

        public void setReceiverEmail(String receiverEmail) {
            this.receiverEmail = receiverEmail;
        }

        public String getReceiverMobile() {
            return receiverMobile;
        }

        public void setReceiverMobile(String receiverMobile) {
            this.receiverMobile = receiverMobile;
        }

        public String getReceiverAddress() {
            return receiverAddress;
        }

        public void setReceiverAddress(String receiverAddress) {
            this.receiverAddress = receiverAddress;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getBillCode() {
            return billCode;
        }

        public void setBillCode(String billCode) {
            this.billCode = billCode;
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

        public String getInvoiceForm() {
            return invoiceForm;
        }

        public void setInvoiceForm(String invoiceForm) {
            this.invoiceForm = invoiceForm;
        }

        public String getInvoiceSerial() {
            return invoiceSerial;
        }

        public void setInvoiceSerial(String invoiceSerial) {
            this.invoiceSerial = invoiceSerial;
        }
    }

    static class BkavInvoiceDetail {
        private String itemName;
        private String unitName;
        private double quantity;
        private double unitPrice;
        private double amount;
        private double discountAmount;
        private double taxRate;
        private String taxType;

        // Getters and Setters
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
    }

    static class BkavCommandResult {
        private boolean success;
        private String errorCode;
        private String errorMessage;
        private String invoiceId;
        private String invoiceNumber;
        private String invoiceSerial;
        private String signedDate;
        private String pdfUrl;
        private String xmlUrl;

        // Getters and Setters
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

        public String getInvoiceSerial() {
            return invoiceSerial;
        }

        public void setInvoiceSerial(String invoiceSerial) {
            this.invoiceSerial = invoiceSerial;
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
}