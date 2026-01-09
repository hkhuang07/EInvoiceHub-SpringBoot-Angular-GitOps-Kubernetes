package com.einvoicehub.core.service;

import com.einvoicehub.core.entity.jpa.InvoicePayload;
import com.einvoicehub.core.repository.jpa.InvoicePayloadRepository;
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
        log.info("Đang lưu payload hóa đơn cho clientRequestId: {}",
                invoicePayload.getClientRequestId());
        InvoicePayload savedPayload = invoicePayloadRepository.save(invoicePayload);
        log.info("Đã lưu payload hóa đơn thành công với ID: {}", savedPayload.getId());
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
        log.info("Đang cập nhật payload hóa đơn cho clientRequestId: {} với trạng thái: {}",
                clientRequestId, status);
        Optional<InvoicePayload> existingPayload = invoicePayloadRepository
                .findByClientRequestId(clientRequestId);
        if (existingPayload.isEmpty()) {
            log.error("Không tìm thấy payload với clientRequestId: {}", clientRequestId);
            throw new RuntimeException("Không tìm thấy payload với clientRequestId: " + clientRequestId);
        }
        InvoicePayload payload = existingPayload.get();
        payload.setResponseRaw(responseData);
        payload.setStatus(status);
        payload.setUpdatedAt(LocalDateTime.now());
        InvoicePayload updatedPayload = invoicePayloadRepository.save(payload);
        log.info("Đã cập nhật payload hóa đơn thành công cho clientRequestId: {}", clientRequestId);
        return updatedPayload;
    }

    // Trong các phương thức updateStatus, updateInvoiceId, updateXmlContent...
    @Transactional
    public void updateStatus(String clientRequestId, String status) {
        InvoicePayload payload = invoicePayloadRepository
                .findByClientRequestId(clientRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.VALIDATION_ERROR, "Không tìm thấy Payload với requestId: " + clientRequestId));
        payload.setStatus(status);
        payload.setUpdatedAt(LocalDateTime.now());
        invoicePayloadRepository.save(payload);
    }

    @Transactional
    public void updateInvoiceId(String clientRequestId, Long invoiceId) {
        log.info("Cập nhật invoiceId: {} cho clientRequestId: {}", invoiceId, clientRequestId);
        InvoicePayload payload = invoicePayloadRepository
                .findByClientRequestId(clientRequestId)
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy payload với clientRequestId: " + clientRequestId));
        payload.setInvoiceId(invoiceId);
        payload.setUpdatedAt(LocalDateTime.now());
        invoicePayloadRepository.save(payload);
    }

    @Transactional
    public void updateXmlContent(String clientRequestId, String xmlContent) {
        log.info("Cập nhật XML content cho clientRequestId: {}", clientRequestId);
        InvoicePayload payload = invoicePayloadRepository
                .findByClientRequestId(clientRequestId)
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy payload với clientRequestId: " + clientRequestId));
        payload.setXmlContent(xmlContent);
        payload.setUpdatedAt(LocalDateTime.now());
        invoicePayloadRepository.save(payload);
    }

    @Transactional
    public void updateSignedXml(String clientRequestId, String signedXml) {
        log.info("Cập nhật Signed XML cho clientRequestId: {}", clientRequestId);
        InvoicePayload payload = invoicePayloadRepository
                .findByClientRequestId(clientRequestId)
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy payload với clientRequestId: " + clientRequestId));
        payload.setSignedXml(signedXml);
        payload.setUpdatedAt(LocalDateTime.now());
        invoicePayloadRepository.save(payload);
    }

    @Transactional
    public void updateJsonContent(String clientRequestId, String jsonContent) {
        log.info("Cập nhật JSON content cho clientRequestId: {}", clientRequestId);
        InvoicePayload payload = invoicePayloadRepository
                .findByClientRequestId(clientRequestId)
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy payload với clientRequestId: " + clientRequestId));
        payload.setJsonContent(jsonContent);
        payload.setUpdatedAt(LocalDateTime.now());
        invoicePayloadRepository.save(payload);
    }

    public Optional<InvoicePayload> findByClientRequestId(String clientRequestId) {
        log.debug("Đang tìm kiếm payload với clientRequestId: {}", clientRequestId);
        return invoicePayloadRepository.findByClientRequestId(clientRequestId);
    }

    public Optional<InvoicePayload> findByTransactionId(String transactionId) {
        log.debug("Đang tìm kiếm payload với transactionId: {}", transactionId);
        return invoicePayloadRepository.findByTransactionId(transactionId);
    }

    public Optional<InvoicePayload> findByInvoiceId(Long invoiceId) {
        log.debug("Đang tìm kiếm payload với invoiceId: {}", invoiceId);
        return invoicePayloadRepository.findByInvoiceId(invoiceId);
    }

    public Page<InvoicePayload> findByMerchantId(Long merchantId, Pageable pageable) {
        log.debug("Đang lấy danh sách payload cho merchantId: {}", merchantId);
        return invoicePayloadRepository.findByMerchantIdOrderByCreatedAtDesc(merchantId, pageable);
    }

    public List<InvoicePayload> findAllByMerchantId(Long merchantId) {
        log.debug("Đang lấy tất cả payload cho merchantId: {}", merchantId);
        return invoicePayloadRepository.findByMerchantId(merchantId);
    }

    public List<InvoicePayload> findByMerchantIdAndDateRange(
            Long merchantId,
            LocalDateTime fromDate,
            LocalDateTime toDate) {
        log.debug("Tìm payload merchantId: {} từ {} đến {}", merchantId, fromDate, toDate);
        return invoicePayloadRepository.findByMerchantIdAndCreatedAtBetween(
                merchantId, fromDate, toDate);
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
            log.error("Lỗi khi chuyển đổi dữ liệu sang JSON", e);
            return null;
        }
    }
}
