package com.einvoicehub.core.service;

import com.einvoicehub.core.entity.mongodb.InvoicePayload;
import com.einvoicehub.core.repository.mongodb.InvoicePayloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service xử lý các thao tác liên quan đến payload hóa đơn trên MongoDB.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvoicePayloadService {

    // CẬP NHẬT: Sử dụng InvoicePayloadRepo đã đổi tên ở trên
    private final InvoicePayloadRepository invoicePayloadRepo;

    /**
     * Lưu trữ payload hóa đơn vào MongoDB.
     */
    public InvoicePayload savePayload(InvoicePayload invoicePayload) {
        log.info("Đang lưu payload hóa đơn cho clientRequestId: {}",
                invoicePayload.getClientRequestId());
        InvoicePayload savedPayload = invoicePayloadRepo.save(invoicePayload);
        log.info("Đã lưu payload hóa đơn thành công với ID: {}", savedPayload.getId());
        return savedPayload;
    }

    /**
     * Cập nhật payload hóa đơn với kết quả từ provider.
     */
    public InvoicePayload updatePayloadWithResponse(
            String requestId,
            String responseData,
            String status) {
        log.info("Đang cập nhật phản hồi cho requestId: {} với trạng thái: {}",
                requestId, status);

        // ĐÃ KHẮC PHỤC: Gọi đúng tên hàm findByClientRequestId
        Optional<InvoicePayload> existingPayload = invoicePayloadRepo
                .findByClientRequestId(requestId);

        if (existingPayload.isEmpty()) {
            log.error("Không tìm thấy payload với requestId: {}", requestId);
            // ĐÃ KHẮC PHỤC: Sử dụng biến requestId thay vì transactionId không tồn tại
            throw new RuntimeException("Không tìm thấy payload với requestId: " + requestId);
        }

        InvoicePayload payload = existingPayload.get();
        // CẬP NHẬT: Đảm bảo setResponseRaw khớp với tên trường trong Entity của bạn
        payload.setResponseRaw(responseData);
        payload.setStatus(status);

        InvoicePayload updatedPayload = invoicePayloadRepo.save(payload);
        log.info("Đã cập nhật payload thành công cho requestId: {}", requestId);
        return updatedPayload;
    }

    /**
     * Tìm kiếm payload hóa đơn theo transactionId.
     */
    public Optional<InvoicePayload> findByTransactionId(String transactionId) {
        log.debug("Đang tìm kiếm payload với transactionId: {}", transactionId);
        return invoicePayloadRepo.findByTransactionId(transactionId);
    }
}