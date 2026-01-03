package com.einvoicehub.core.service;

import com.einvoicehub.core.adapter.InvoiceProvider;
import com.einvoicehub.core.common.exception.InsufficientQuotaException;
import com.einvoicehub.core.common.exception.MerchantNotFoundException;
import com.einvoicehub.core.dto.InvoiceRequest;
import com.einvoicehub.core.dto.InvoiceResponse;
import com.einvoicehub.core.entity.InvoiceMetadata;
import com.einvoicehub.core.entity.InvoicePayload;
import com.einvoicehub.core.entity.Merchant;
import com.einvoicehub.core.repository.InvoiceMetadataRepository;
import com.einvoicehub.core.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

/**
 * Service chính điều phối toàn bộ quy trình tạo hóa đơn điện tử.
 * Service này đóng vai trò là orchestration layer, điều phối các bước
 * từ xác thực merchant, kiểm tra hạn mức, lưu trữ payload, gọi provider,
 * và cập nhật trạng thái cuối cùng.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final MerchantRepository merchantRepository;
    private final InvoiceMetadataRepository invoiceMetadataRepository;
    private final InvoicePayloadService invoicePayloadService;
    private final Map<String, InvoiceProvider> invoiceProviderMap;

    /**
     * Phương thức chính để tạo hóa đơn điện tử.
     * Thực hiện các bước theo thứ tự:
     * 1. Xác thực và kiểm tra merchant
     * 2. Kiểm tra hạn mức hóa đơn
     * 3. Tạo InvoiceMetadata với trạng thái PENDING
     * 4. Lưu payload thô vào MongoDB
     * 5. Gọi provider tương ứng để tạo hóa đơn
     * 6. Cập nhật trạng thái và payload với kết quả
     *
     * @param request yêu cầu tạo hóa đơn từ client
     * @return InvoiceResponse chứa kết quả tạo hóa đơn
     */
    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        String transactionId = UUID.randomUUID().toString();
        log.info("Bắt đầu quy trình tạo hóa đơn với transactionId: {}, provider: {}",
                transactionId, request.getProviderCode());

        // Bước 1: Xác thực merchant
        Merchant merchant = merchantRepository.findByCode(request.getMerchantCode())
                .orElseThrow(() -> new MerchantNotFoundException(request.getMerchantCode()));
        log.info("Đã xác thực merchant thành công: {}", merchant.getCode());

        // Bước 2: Kiểm tra hạn mức hóa đơn
        if (!canCreateMoreInvoices(merchant)) {
            log.warn("Merchant {} đã vượt quá hạn mức hóa đơn. " +
                    "Hạn mức: {}, Đã sử dụng: {}",
                    merchant.getCode(),
                    merchant.getInvoiceQuota(),
                    merchant.getCurrentInvoiceCount());
            throw new InsufficientQuotaException(
                    String.format("Merchant %s đã vượt quá hạn mức hóa đơn. " +
                                    "Hạn mức: %d, Đã sử dụng: %d",
                            merchant.getCode(),
                            merchant.getInvoiceQuota(),
                            merchant.getCurrentInvoiceCount())
            );
        }
        log.info("Merchant {} đủ điều kiện tạo hóa đơn. " +
                        "Hạn mức: {}, Đã sử dụng: {}",
                merchant.getCode(),
                merchant.getInvoiceQuota(),
                merchant.getCurrentInvoiceCount());

        // Bước 3: Tạo InvoiceMetadata với trạng thái PENDING
        InvoiceMetadata invoiceMetadata = InvoiceMetadata.builder()
                .transactionId(transactionId)
                .merchantCode(request.getMerchantCode())
                .providerCode(request.getProviderCode())
                .invoiceNumber(request.getInvoiceData().getInvoiceNumber())
                .status("PENDING")
                .createdAt(java.time.LocalDateTime.now())
                .build();
        invoiceMetadata = invoiceMetadataRepository.save(invoiceMetadata);
        log.info("Đã tạo InvoiceMetadata với ID: {}, trạng thái: {}",
                invoiceMetadata.getId(), invoiceMetadata.getStatus());

        // Bước 4: Lưu payload thô vào MongoDB
        InvoicePayload invoicePayload = InvoicePayload.builder()
                .transactionId(transactionId)
                .merchantCode(request.getMerchantCode())
                .providerCode(request.getProviderCode())
                .requestData(request.toString())
                .status("PENDING")
                .createdAt(java.time.LocalDateTime.now())
                .build();
        invoicePayloadService.savePayload(invoicePayload);
        log.info("Đã lưu payload hóa đơn vào MongoDB cho transactionId: {}", transactionId);

        try {
            // Bước 5: Gọi provider tương ứng để tạo hóa đơn
            InvoiceProvider provider = getProvider(request.getProviderCode());
            log.info("Đang gọi provider: {} cho transactionId: {}",
                    request.getProviderCode(), transactionId);
            InvoiceResponse providerResponse = provider.createInvoice(request);
            log.info("Provider {} trả về kết quả thành công cho transactionId: {}",
                    request.getProviderCode(), transactionId);

            // Bước 6a: Cập nhật trạng thái thành công trong MySQL
            invoiceMetadata.setStatus("SUCCESS");
            invoiceMetadata.setProviderInvoiceId(providerResponse.getInvoiceId());
            invoiceMetadata.setCompletedAt(java.time.LocalDateTime.now());
            invoiceMetadataRepository.save(invoiceMetadata);
            log.info("Đã cập nhật InvoiceMetadata thành SUCCESS cho transactionId: {}", transactionId);

            // Bước 6b: Cập nhật payload trong MongoDB với kết quả thành công
            invoicePayloadService.updatePayloadWithResponse(
                    transactionId,
                    providerResponse.toString(),
                    "SUCCESS"
            );
            log.info("Đã cập nhật payload thành SUCCESS cho transactionId: {}", transactionId);

            // Bước 7: Tăng số lượng hóa đơn đã sử dụng của merchant
            incrementInvoiceUsage(merchant);
            log.info("Đã tăng số lượng hóa đơn sử dụng của merchant {} lên {}",
                    merchant.getCode(), merchant.getCurrentInvoiceCount());

            // Trả về response cho client
            return InvoiceResponse.builder()
                    .success(true)
                    .transactionId(transactionId)
                    .invoiceId(providerResponse.getInvoiceId())
                    .message("Hóa đơn đã được tạo thành công")
                    .build();

        } catch (Exception e) {
            log.error("Lỗi khi tạo hóa đơn cho transactionId: {}: {}",
                    transactionId, e.getMessage(), e);

            // Cập nhật trạng thái thất bại trong MySQL
            invoiceMetadata.setStatus("FAILED");
            invoiceMetadata.setErrorMessage(e.getMessage());
            invoiceMetadata.setCompletedAt(java.time.LocalDateTime.now());
            invoiceMetadataRepository.save(invoiceMetadata);
            log.info("Đã cập nhật InvoiceMetadata thành FAILED cho transactionId: {}", transactionId);

            // Cập nhật payload trong MongoDB với kết quả thất bại
            invoicePayloadService.updatePayloadWithResponse(
                    transactionId,
                    e.getMessage(),
                    "FAILED"
            );
            log.info("Đã cập nhật payload thành FAILED cho transactionId: {}", transactionId);

            // Ném exception để GlobalExceptionHandler xử lý
            throw e;
        }
    }

    /**
     * Lấy provider tương ứng từ Map dựa trên provider code.
     *
     * @param providerCode mã provider (ví dụ: "MISA", "BKAV", "VIETTEL")
     * @return InvoiceProvider tương ứng
     * @throws RuntimeException nếu không tìm thấy provider
     */
    private InvoiceProvider getProvider(String providerCode) {
        InvoiceProvider provider = invoiceProviderMap.get(providerCode.toUpperCase());
        if (provider == null) {
            log.error("Không tìm thấy provider với mã: {}", providerCode);
            throw new RuntimeException("Không hỗ trợ provider: " + providerCode);
        }
        return provider;
    }

    /**
     * Kiểm tra xem merchant có thể tạo thêm hóa đơn hay không.
     *
     * @param merchant merchant cần kiểm tra
     * @return true nếu merchant có thể tạo thêm hóa đơn, false nếu đã vượt hạn mức
     */
    public boolean canCreateMoreInvoices(Merchant merchant) {
        return merchant.getCurrentInvoiceCount() < merchant.getInvoiceQuota();
    }

    /**
     * Tăng số lượng hóa đơn đã sử dụng của merchant lên 1 đơn vị.
     *
     * @param merchant merchant cần cập nhật
     */
    public void incrementInvoiceUsage(Merchant merchant) {
        merchant.setCurrentInvoiceCount(merchant.getCurrentInvoiceCount() + 1);
        merchantRepository.save(merchant);
    }
}
