package com.einvoicehub.service;

import com.einvoicehub.model.request.InvoiceRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * Invoice Payload Service - MongoDB Operations
 * 
 * Service cho việc lưu trữ và truy vấn payload hóa đơn trong MongoDB
 */
@Service
public class InvoicePayloadService {

    private static final Logger logger = LoggerFactory.getLogger(InvoicePayloadService.class);
    private static final String COLLECTION_NAME = "invoice_payloads";

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    public InvoicePayloadService(MongoTemplate mongoTemplate, ObjectMapper objectMapper) {
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Lưu payload hóa đơn vào MongoDB
     * 
     * @param clientRequestId Client request ID
     * @param request Invoice request
     * @return Document ID
     */
    public String saveInvoicePayload(String clientRequestId, InvoiceRequest request) {
        try {
            InvoicePayloadDocument document = new InvoicePayloadDocument();
            document.setClientRequestId(clientRequestId);
            document.setRequestJson(objectMapper.writeValueAsString(request));
            document.setCreatedAt(LocalDateTime.now());
            
            // Lưu metadata từ request
            document.setMerchantTaxCode(request.getSellerTaxCode());
            document.setProviderCode(request.getProviderCode());
            document.setInvoiceDate(request.getInvoiceDate());
            document.setTotalAmount(request.getGrandTotalAmount());
            
            InvoicePayloadDocument saved = mongoTemplate.save(document, COLLECTION_NAME);
            logger.debug("Saved invoice payload. clientRequestId: {}, documentId: {}", 
                clientRequestId, saved.getId());
            
            return saved.getId();
            
        } catch (JsonProcessingException e) {
            logger.error("Error serializing invoice request. clientRequestId: {}", clientRequestId, e);
            throw new RuntimeException("Error saving invoice payload", e);
        }
    }

    /**
     * Lưu response từ provider
     * 
     * @param clientRequestId Client request ID
     * @param providerResponse JSON response từ provider
     */
    public void saveProviderResponse(String clientRequestId, String providerResponse) {
        Query query = new Query(Criteria.where("clientRequestId").is(clientRequestId));
        Update update = new Update()
            .set("responseJson", providerResponse)
            .set("responseReceivedAt", LocalDateTime.now());
        
        mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        logger.debug("Saved provider response. clientRequestId: {}", clientRequestId);
    }

    /**
     * Lấy payload theo client request ID
     */
    public Optional<InvoicePayloadDocument> getPayloadByClientRequestId(String clientRequestId) {
        Query query = new Query(Criteria.where("clientRequestId").is(clientRequestId));
        InvoicePayloadDocument document = mongoTemplate.findOne(query, InvoicePayloadDocument.class, COLLECTION_NAME);
        return Optional.ofNullable(document);
    }

    /**
     * Lấy request object từ payload
     */
    public Optional<InvoiceRequest> getInvoiceRequest(String clientRequestId) {
        return getPayloadByClientRequestId(clientRequestId)
            .map(doc -> {
                try {
                    return objectMapper.readValue(doc.getRequestJson(), InvoiceRequest.class);
                } catch (JsonProcessingException e) {
                    logger.error("Error deserializing invoice request. clientRequestId: {}", clientRequestId, e);
                    return null;
                }
            });
    }

    /**
     * Cập nhật trạng thái payload
     */
    public void updatePayloadStatus(String clientRequestId, String status, String message) {
        Query query = new Query(Criteria.where("clientRequestId").is(clientRequestId));
        Update update = new Update()
            .set("processingStatus", status)
            .set("statusMessage", message)
            .set("updatedAt", LocalDateTime.now());
        
        mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    }

    /**
     * Xóa payload cũ (cleanup)
     */
    public long deleteOldPayloads(LocalDateTime beforeDate) {
        Query query = new Query(Criteria.where("createdAt").lt(beforeDate));
        var result = mongoTemplate.remove(query, COLLECTION_NAME);
        logger.info("Deleted {} old invoice payloads", result.getDeletedCount());
        return result.getDeletedCount();
    }

    /**
     * Inner class - Document structure for MongoDB
     */
    public static class InvoicePayloadDocument {
        private String id;
        private String clientRequestId;
        private String requestJson;
        private String responseJson;
        private LocalDateTime createdAt;
        private LocalDateTime responseReceivedAt;
        private LocalDateTime updatedAt;
        
        // Metadata
        private String merchantTaxCode;
        private String providerCode;
        private LocalDateTime invoiceDate;
        private java.math.BigDecimal totalAmount;
        private String processingStatus;
        private String statusMessage;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getClientRequestId() {
            return clientRequestId;
        }

        public void setClientRequestId(String clientRequestId) {
            this.clientRequestId = clientRequestId;
        }

        public String getRequestJson() {
            return requestJson;
        }

        public void setRequestJson(String requestJson) {
            this.requestJson = requestJson;
        }

        public String getResponseJson() {
            return responseJson;
        }

        public void setResponseJson(String responseJson) {
            this.responseJson = responseJson;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDateTime getResponseReceivedAt() {
            return responseReceivedAt;
        }

        public void setResponseReceivedAt(LocalDateTime responseReceivedAt) {
            this.responseReceivedAt = responseReceivedAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getMerchantTaxCode() {
            return merchantTaxCode;
        }

        public void setMerchantTaxCode(String merchantTaxCode) {
            this.merchantTaxCode = merchantTaxCode;
        }

        public String getProviderCode() {
            return providerCode;
        }

        public void setProviderCode(String providerCode) {
            this.providerCode = providerCode;
        }

        public LocalDateTime getInvoiceDate() {
            return invoiceDate;
        }

        public void setInvoiceDate(LocalDateTime invoiceDate) {
            this.invoiceDate = invoiceDate;
        }

        public java.math.BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(java.math.BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getProcessingStatus() {
            return processingStatus;
        }

        public void setProcessingStatus(String processingStatus) {
            this.processingStatus = processingStatus;
        }

        public String getStatusMessage() {
            return statusMessage;
        }

        public void setStatusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
        }
    }
}