package com.einvoicehub.core.service;

import com.einvoicehub.core.entity.InvoicePayload;
import com.einvoicehub.core.repository.InvoicePayloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service xử lý các thao tác liên quan đến payload hóa đơn trên MongoDB.
 * Chịu trách nhiệm lưu trữ và truy vấn dữ liệu thô (raw request/response)
 * của các giao dịch hóa đơn điện tử phục vụ cho mục đích kiểm toán.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvoicePayloadService {

    private final InvoicePayloadRepository invoicePayloadRepository;

    /**
     * Lưu trữ payload hóa đơn vào MongoDB.
     * Phương thức này thường được gọi sau khi tạo InvoiceMetadata với trạng thái PENDING
     * để lưu trữ dữ liệu thô của yêu cầu trước khi thực hiện gọi API tới provider.
     *
     * @param invoicePayload entity InvoicePayload cần lưu
     * @return InvoicePayload đã được lưu với ID được sinh ra
     */
    public InvoicePayload savePayload(InvoicePayload invoicePayload) {
        log.info("Đang lưu payload hóa đơn cho transactionId: {}",
                invoicePayload.getTransactionId());
        InvoicePayload savedPayload = invoicePayloadRepository.save(invoicePayload);
        log.info("Đã lưu payload hóa đơn thành công với ID: {}", savedPayload.getId());
        return savedPayload;
    }

    /**
     * Cập nhật payload hóa đơn với kết quả từ provider.
     * Phương thức này được gọi sau khi nhận được phản hồi từ provider
     * để cập nhật trạng thái và dữ liệu phản hồi.
     *
     * @param transactionId ID giao dịch cần cập nhật
     * @param responseData dữ liệu phản hồi từ provider
     * @param status trạng thái kết quả (SUCCESS hoặc FAILED)
     * @return InvoicePayload đã được cập nhật
     */
    public InvoicePayload updatePayloadWithResponse(
            String transactionId,
            String responseData,
            String status) {
        log.info("Đang cập nhật payload hóa đơn cho transactionId: {} với trạng thái: {}",
                transactionId, status);
        Optional<InvoicePayload> existingPayload = invoicePayloadRepository
                .findByTransactionId(transactionId);
        if (existingPayload.isEmpty()) {
            log.error("Không tìm thấy payload với transactionId: {}", transactionId);
            throw new RuntimeException("Không tìm thấy payload với transactionId: " + transactionId);
        }
        InvoicePayload payload = existingPayload.get();
        payload.setResponseData(responseData);
        payload.setStatus(status);
        InvoicePayload updatedPayload = invoicePayloadRepository.save(payload);
        log.info("Đã cập nhật payload hóa đơn thành công cho transactionId: {}", transactionId);
        return updatedPayload;
    }

    /**
     * Tìm kiếm payload hóa đơn theo transactionId.
     *
     * @param transactionId ID giao dịch cần tìm
     * @return Optional chứa InvoicePayload nếu tìm thấy, ngược lại là Optional empty
     */
    public Optional<InvoicePayload> findByTransactionId(String transactionId) {
        log.debug("Đang tìm kiếm payload với transactionId: {}", transactionId);
        return invoicePayloadRepository.findByTransactionId(transactionId);
    }
}
