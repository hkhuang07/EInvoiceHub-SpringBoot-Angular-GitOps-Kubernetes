package com.einvoicehub.core.service;

import com.einvoicehub.core.entity.jpa.InvoicePayload;
import com.einvoicehub.core.repository.jpa.InvoicePayloadRepository;
import com.einvoicehub.core.common.exception.AppException;
import com.einvoicehub.core.common.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoicePayloadService {

    private final InvoicePayloadRepository invoicePayloadRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public InvoicePayload savePayload(InvoicePayload invoicePayload) {
        log.info("Saving invoice payload for clientRequestId: {}", invoicePayload.getClientRequestId());
        InvoicePayload savedPayload = invoicePayloadRepository.save(invoicePayload);
        log.info("Invoice payload saved successfully with ID: {}", savedPayload.getId());
        return savedPayload;
    }

    @Transactional
    public InvoicePayload createPayloadFromRequest(
            Long merchantId,
            String clientRequestId,
            String providerCode,
            Map<String, Object> requestData) {
        InvoicePayload payload = InvoicePayload.builder()
                .merchantId(merchantId)
                .clientRequestId(clientRequestId)
                .providerCode(providerCode)
                .rawData(convertToJson(requestData))
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
        return savePayload(payload);
    }

    @Transactional
    public InvoicePayload updatePayloadWithResponse(
            String clientRequestId,
            String responseData,
            String status) {
        log.info("Updating invoice payload for clientRequestId: {} with status: {}", clientRequestId, status);

        InvoicePayload payload = invoicePayloadRepository.findByClientRequestId(clientRequestId)
                .orElseThrow(() -> {
                    log.error("Invoice payload not found with clientRequestId: {}", clientRequestId);
                    return new AppException(ErrorCode.VALIDATION_ERROR,
                            "Invoice payload not found with clientRequestId: " + clientRequestId);
                });

        payload.setResponseRaw(responseData);
        payload.setStatus(status);
        payload.setUpdatedAt(LocalDateTime.now());
        InvoicePayload updatedPayload = invoicePayloadRepository.save(payload);
        log.info("Invoice payload updated successfully for clientRequestId: {}", clientRequestId);
        return updatedPayload;
    }

    @Transactional
    public void updateStatus(String clientRequestId, String status) {
        InvoicePayload payload = invoicePayloadRepository.findByClientRequestId(clientRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.VALIDATION_ERROR,
                        "Invoice payload not found with requestId: " + clientRequestId));

        payload.setStatus(status);
        payload.setUpdatedAt(LocalDateTime.now());
        invoicePayloadRepository.save(payload);
    }

    @Transactional
    public void updateInvoiceId(String clientRequestId, Long invoiceId) {
        log.info("Updating invoiceId: {} for clientRequestId: {}", invoiceId, clientRequestId);

        InvoicePayload payload = invoicePayloadRepository.findByClientRequestId(clientRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.VALIDATION_ERROR,
                        "Invoice payload not found with clientRequestId: " + clientRequestId));

        payload.setInvoiceId(invoiceId);
        payload.setUpdatedAt(LocalDateTime.now());
        invoicePayloadRepository.save(payload);
    }

    @Transactional
    public void updateXmlContent(String clientRequestId, String xmlContent) {
        log.info("Updating XML content for clientRequestId: {}", clientRequestId);

        InvoicePayload payload = invoicePayloadRepository.findByClientRequestId(clientRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.VALIDATION_ERROR,
                        "Invoice payload not found with clientRequestId: " + clientRequestId));

        payload.setXmlContent(xmlContent);
        payload.setUpdatedAt(LocalDateTime.now());
        invoicePayloadRepository.save(payload);
    }

    @Transactional
    public void updateSignedXml(String clientRequestId, String signedXml) {
        log.info("Updating Signed XML for clientRequestId: {}", clientRequestId);

        InvoicePayload payload = invoicePayloadRepository.findByClientRequestId(clientRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.VALIDATION_ERROR,
                        "Invoice payload not found with clientRequestId: " + clientRequestId));

        payload.setSignedXml(signedXml);
        payload.setUpdatedAt(LocalDateTime.now());
        invoicePayloadRepository.save(payload);
    }

    @Transactional
    public void updateJsonContent(String clientRequestId, String jsonContent) {
        log.info("Updating JSON content for clientRequestId: {}", clientRequestId);

        InvoicePayload payload = invoicePayloadRepository.findByClientRequestId(clientRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.VALIDATION_ERROR,
                        "Invoice payload not found with clientRequestId: " + clientRequestId));

        payload.setJsonContent(jsonContent);
        payload.setUpdatedAt(LocalDateTime.now());
        invoicePayloadRepository.save(payload);
    }

    public Optional<InvoicePayload> findByClientRequestId(String clientRequestId) {
        log.debug("Searching for payload with clientRequestId: {}", clientRequestId);
        return invoicePayloadRepository.findByClientRequestId(clientRequestId);
    }

    public Optional<InvoicePayload> findByTransactionId(String transactionId) {
        log.debug("Searching for payload with transactionId: {}", transactionId);
        return invoicePayloadRepository.findByTransactionId(transactionId);
    }

    public Optional<InvoicePayload> findByInvoiceId(Long invoiceId) {
        log.debug("Searching for payload with invoiceId: {}", invoiceId);
        return invoicePayloadRepository.findByInvoiceId(invoiceId);
    }

    public Page<InvoicePayload> findByMerchantId(Long merchantId, Pageable pageable) {
        log.debug("Fetching payload list for merchantId: {}", merchantId);
        return invoicePayloadRepository.findByMerchantIdOrderByCreatedAtDesc(merchantId, pageable);
    }

    public List<InvoicePayload> findAllByMerchantId(Long merchantId) {
        log.debug("Fetching all payloads for merchantId: {}", merchantId);
        return invoicePayloadRepository.findByMerchantId(merchantId);
    }

    public List<InvoicePayload> findByMerchantIdAndDateRange(
            Long merchantId,
            LocalDateTime fromDate,
            LocalDateTime toDate) {
        log.debug("Finding payloads for merchantId: {} from {} to {}", merchantId, fromDate, toDate);
        return invoicePayloadRepository.findByMerchantIdAndCreatedAtBetween(merchantId, fromDate, toDate);
    }

    public long countByMerchantId(Long merchantId) {
        return invoicePayloadRepository.countByMerchantId(merchantId);
    }

    public long countByMerchantIdAndStatus(Long merchantId, String status) {
        return invoicePayloadRepository.countByMerchantIdAndStatus(merchantId, status);
    }

    private String convertToJson(Map<String, Object> data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("Error converting data to JSON", e);
            return null;
        }
    }
}