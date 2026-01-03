package com.einvoicehub.core.service;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.entity.mongodb.InvoicePayload;
import com.einvoicehub.core.entity.mysql.InvoiceMetadata;
import com.einvoicehub.core.entity.mysql.Merchant;
import com.einvoicehub.core.provider.InvoiceProvider;
import com.einvoicehub.core.provider.InvoiceRequest;
import com.einvoicehub.core.provider.InvoiceResponse;
import com.einvoicehub.core.provider.ProviderConfig;
import com.einvoicehub.core.repository.mysql.InvoiceMetadataRepository;
import com.einvoicehub.core.repository.mysql.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final MerchantRepository merchantRepo;
    private final InvoiceMetadataRepository invoiceMetadataRepo;
    private final InvoicePayloadService invoicePayloadService;
    private final Map<String, InvoiceProvider> providers;

    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        String requestId = request.getClientRequestId();
        log.info("Bắt đầu tạo hóa đơn cho requestId: {}", requestId);

        // 1. Xác thực Merchant
        Merchant merchant = merchantRepo.findById(request.getInvoiceMetadataId())
                .orElseThrow(() -> new RuntimeException("Merchant not found"));

        // 2. Kiểm tra Quota (Sửa lỗi image_6baf9e dòng 125)
        if (merchant.getCurrentInvoiceCount() >= merchant.getInvoiceQuota()) {
            throw new RuntimeException("Merchant đã hết hạn mức hóa đơn.");
        }

        // 3. Khởi tạo Metadata (MySQL) - Đã fix trường providerCode
        InvoiceMetadata metadata = InvoiceMetadata.builder()
                .merchant(merchant)
                .clientRequestId(requestId)
                .providerCode(request.getProviderCode())
                .status(InvoiceStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        metadata = invoiceMetadataRepo.save(metadata);

        // 4. Lưu Payload (MongoDB)
        InvoicePayload payload = InvoicePayload.builder()
                .clientRequestId(requestId)
                .merchantId(merchant.getId())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
        invoicePayloadService.savePayload(payload);

        try {
            // 5. Gọi Provider Adapter
            InvoiceProvider provider = getProvider(request.getProviderCode());
            ProviderConfig config = ProviderConfig.builder().build(); // Cần lấy config thực tế từ DB

            // FIX: Gọi đúng hàm issueInvoice
            InvoiceResponse response = provider.issueInvoice(request, config);

            // 6. Xử lý kết quả (Sửa lỗi isSuccess)
            if (response.getStatus() == InvoiceResponse.ResponseStatus.SUCCESS) {
                handleSuccess(metadata, response, merchant, requestId);
            } else {
                handleFailure(metadata, response.getErrorMessage(), response.getErrorCode(), requestId);
            }
            return response;

        } catch (Exception e) {
            log.error("Lỗi phát hành hóa đơn: {}", e.getMessage());
            handleFailure(metadata, e.getMessage(), "SYSTEM_ERROR", requestId);
            throw e;
        }
    }

    private void handleSuccess(InvoiceMetadata metadata, InvoiceResponse res, Merchant merchant, String requestId) {
        // Sử dụng Business Method có sẵn trong InvoiceMetadata.java của Huy
        metadata.markAsSuccess(res.getTransactionCode());
        metadata.setInvoiceNumber(res.getInvoiceNumber());
        invoiceMetadataRepo.save(metadata);

        // Cập nhật MongoDB
        invoicePayloadService.updatePayloadWithResponse(requestId, "SUCCESS_RESPONSE", "SUCCESS");

        // Tăng usage (Sửa lỗi image_6baf9e dòng 129)
        merchant.setCurrentInvoiceCount(merchant.getCurrentInvoiceCount() + 1);
        merchantRepo.save(merchant);
    }

    private void handleFailure(InvoiceMetadata metadata, String error, String errorCode, String requestId) {
        // Sử dụng Business Method markAsFailed của Huy
        metadata.markAsFailed(errorCode, error);
        invoiceMetadataRepo.save(metadata);

        invoicePayloadService.updatePayloadWithResponse(requestId, error, "FAILED");
    }

    private InvoiceProvider getProvider(String code) {
        InvoiceProvider provider = providers.get(code.toUpperCase());
        if (provider == null) throw new RuntimeException("Provider not supported: " + code);
        return provider;
    }
}