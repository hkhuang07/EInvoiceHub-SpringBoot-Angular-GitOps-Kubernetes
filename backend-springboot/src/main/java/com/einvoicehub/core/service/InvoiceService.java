package com.einvoicehub.core.service;

import com.einvoicehub.core.common.exception.AppException;
import com.einvoicehub.core.common.exception.ErrorCode;
import com.einvoicehub.core.dto.request.InvoiceRequest;  // Public DTO
import com.einvoicehub.core.dto.response.InvoiceResponse; // Public DTO
import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.entity.mongodb.InvoicePayload;
import com.einvoicehub.core.entity.mysql.InvoiceMetadata;
import com.einvoicehub.core.entity.mysql.Merchant;
import com.einvoicehub.core.provider.InvoiceProvider;
import com.einvoicehub.core.provider.ProviderConfig;
import com.einvoicehub.core.repository.mysql.InvoiceMetadataRepository;
import com.einvoicehub.core.repository.mysql.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * InvoiceService - Động cơ điều phối chính của EInvoiceHub.
 * Đã hợp nhất logic nghiệp vụ và hệ thống quản lý Exception chuyên nghiệp.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final MerchantRepository merchantRepo;
    private final InvoiceMetadataRepository invoiceMetadataRepo;
    private final InvoicePayloadService invoicePayloadService;
    private final Map<String, InvoiceProvider> providers;

    /**
     * Quy trình phát hành hóa đơn hoàn chỉnh (End-to-End Workflow)
     */
    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest publicRequest) {
        // 1. Lấy mã định danh yêu cầu (Idempotency Key)
        String requestId = publicRequest.getInternalReferenceCode();
        log.info("Bắt đầu quy trình phát hành hóa đơn. RequestID: {}", requestId);

        // 2. Xác thực Merchant (Sử dụng AppException chuyên nghiệp)
        Merchant merchant = merchantRepo.findById(publicRequest.getMerchantId())
                .orElseThrow(() -> new AppException(ErrorCode.MERCHANT_NOT_FOUND));

        // 3. Kiểm tra hạn mức (Quota)
        if (merchant.getCurrentInvoiceCount() >= merchant.getInvoiceQuota()) {
            throw new AppException(ErrorCode.INSUFFICIENT_QUOTA,
                    String.format("Merchant %s đã đạt giới hạn %d/%d hóa đơn.",
                            merchant.getName(), merchant.getCurrentInvoiceCount(), merchant.getInvoiceQuota()));
        }

        // 4. Khởi tạo bản ghi Metadata (MySQL) ở trạng thái PENDING
        InvoiceMetadata metadata = initMetadata(merchant, publicRequest, requestId);

        // 5. Lưu trữ Payload thô ban đầu (MongoDB) để phục vụ Audit
        saveInitialPayload(merchant, publicRequest, requestId);

        try {
            // 6. Lấy Provider Adapter tương ứng
            InvoiceProvider provider = getProvider(publicRequest.getProviderCode());

            // 7. Mapping sang Model nội bộ & Chuẩn hóa dữ liệu (Normalize)
            com.einvoicehub.core.provider.model.InvoiceRequest internalRequest = mapToInternalRequest(publicRequest);

            // 8. TODO: Lấy config thực tế từ MerchantProviderConfig (Tài khoản BKAV/MISA của Merchant)
            ProviderConfig config = ProviderConfig.builder().build();

            // 9. Gọi Adapter để tương tác với bên thứ ba
            com.einvoicehub.core.provider.model.InvoiceResponse internalRes = provider.issueInvoice(internalRequest, config);

            // 10. Xử lý kết quả trả về
            if (internalRes.isSuccessful()) {
                handleSuccess(metadata, internalRes, merchant, requestId);
                return mapToPublicResponse(internalRes, true, "Hóa đơn đã được phát hành thành công.");
            } else {
                handleFailure(metadata, internalRes.getErrorMessage(), internalRes.getErrorCode(), requestId);
                return mapToPublicResponse(internalRes, false, "Provider từ chối: " + internalRes.getErrorMessage());
            }

        } catch (Exception e) {
            log.error("Lỗi hệ thống khi phát hành hóa đơn cho Request {}: {}", requestId, e.getMessage());
            handleFailure(metadata, e.getMessage(), "SYSTEM_ERROR", requestId);

            // Re-throw dưới dạng AppException để GlobalExceptionHandler trả về JSON chuẩn cho Client
            throw new AppException(ErrorCode.PROVIDER_ERROR, "Lỗi kết nối Provider: " + e.getMessage(), e);
        }
    }

    // --- CÁC PHƯƠNG THỨC HỖ TRỢ (PRIVATE HELPERS) ---

    private InvoiceMetadata initMetadata(Merchant merchant, InvoiceRequest req, String requestId) {
        InvoiceMetadata metadata = InvoiceMetadata.builder()
                .merchant(merchant)
                .clientRequestId(requestId)
                .providerCode(req.getProviderCode())
                .status(InvoiceStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        return invoiceMetadataRepo.save(metadata);
    }

    private void saveInitialPayload(Merchant merchant, InvoiceRequest req, String requestId) {
        InvoicePayload payload = InvoicePayload.builder()
                .clientRequestId(requestId)
                .merchantId(merchant.getId())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .rawRequest(req) // Lưu nguyên object request để đối soát
                .build();
        invoicePayloadService.savePayload(payload);
    }

    private com.einvoicehub.core.provider.model.InvoiceRequest mapToInternalRequest(InvoiceRequest pub) {
        return com.einvoicehub.core.provider.model.InvoiceRequest.builder()
                .clientRequestId(pub.getInternalReferenceCode())
                .providerCode(pub.getProviderCode())
                .invoiceType(pub.getInvoiceType())
                .issueDate(pub.getInvoiceDate().toLocalDate())
                .seller(com.einvoicehub.core.provider.model.InvoiceRequest.SellerInfo.builder()
                        .name(pub.getSellerName()).taxCode(pub.getSellerTaxCode()).address(pub.getSellerAddress()).build())
                .buyer(com.einvoicehub.core.provider.model.InvoiceRequest.BuyerInfo.builder()
                        .name(pub.getBuyerName()).taxCode(pub.getBuyerTaxCode()).address(pub.getBuyerAddress()).build())
                .items(pub.getItems().stream().map(item -> {
                    item.normalizeData(); // CHUẨN HÓA DỮ LIỆU TÀI CHÍNH
                    return com.einvoicehub.core.provider.model.InvoiceRequest.InvoiceItem.builder()
                            .itemName(item.getItemName())
                            .quantity(item.getQuantity())
                            .unitPrice(item.getUnitPrice())
                            .amount(item.getTotalAmount())
                            .taxRate(item.getTaxRate())
                            .taxAmount(item.getTotalTaxAmount())
                            .build();
                }).collect(Collectors.toList()))
                .summary(com.einvoicehub.core.provider.model.InvoiceRequest.InvoiceSummary.builder()
                        .totalAmount(pub.getGrandTotalAmount())
                        .totalTaxAmount(pub.getTotalTaxAmount())
                        .currencyCode(pub.getCurrency())
                        .build())
                .build();
    }

    private InvoiceResponse mapToPublicResponse(com.einvoicehub.core.provider.model.InvoiceResponse res, boolean success, String msg) {
        return InvoiceResponse.builder()
                .success(success)
                .transactionId(res.getTransactionCode())
                .invoiceNumber(res.getInvoiceNumber())
                .pdfDownloadUrl(res.getPdfUrl())
                .message(msg)
                .errorCode(res.getErrorCode())
                .build();
    }

    private void handleSuccess(InvoiceMetadata metadata, com.einvoicehub.core.provider.model.InvoiceResponse res, Merchant merchant, String requestId) {
        metadata.markAsSuccess(res.getTransactionCode());
        metadata.setInvoiceNumber(res.getInvoiceNumber());
        invoiceMetadataRepo.save(metadata);

        invoicePayloadService.updatePayloadWithResponse(requestId, res.toString(), "SUCCESS");

        merchant.setCurrentInvoiceCount(merchant.getCurrentInvoiceCount() + 1);
        merchantRepo.save(merchant);
    }

    private void handleFailure(InvoiceMetadata metadata, String error, String errorCode, String requestId) {
        metadata.markAsFailed(errorCode, error);
        invoiceMetadataRepo.save(metadata);
        invoicePayloadService.updatePayloadWithResponse(requestId, error, "FAILED");
    }

    private InvoiceProvider getProvider(String code) {
        InvoiceProvider provider = providers.get(code.toUpperCase());
        if (provider == null) {
            throw new AppException(ErrorCode.PROVIDER_ERROR, "Provider không được hỗ trợ: " + code);
        }
        return provider;
    }
}