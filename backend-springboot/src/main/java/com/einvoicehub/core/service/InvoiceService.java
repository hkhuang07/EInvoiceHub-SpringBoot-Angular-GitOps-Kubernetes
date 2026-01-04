package com.einvoicehub.core.service;

import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.entity.mongodb.InvoicePayload;
import com.einvoicehub.core.entity.mysql.InvoiceMetadata;
import com.einvoicehub.core.entity.mysql.Merchant;
import com.einvoicehub.core.dto.request.InvoiceRequest;
import com.einvoicehub.core.dto.response.InvoiceResponse;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final MerchantRepository merchantRepo;
    private final InvoiceMetadataRepository invoiceMetadataRepo;
    private final InvoicePayloadService invoicePayloadService;
    private final Map<String, InvoiceProvider> providers;

    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest publicRequest) {
        String requestId = publicRequest.getInternalReferenceCode(); // Lấy từ DTO Minimax
        log.info("Bắt đầu tạo hóa đơn cho requestId: {}", requestId);

        // 1. Xác thực Merchant
        Merchant merchant = merchantRepo.findById(publicRequest.getMerchantId())
                .orElseThrow(() -> new RuntimeException("Merchant not found"));

        // 2. Kiểm tra Quota
        if (merchant.getCurrentInvoiceCount() >= merchant.getInvoiceQuota()) {
            throw new RuntimeException("Merchant đã hết hạn mức hóa đơn.");
        }

        // 3. Khởi tạo Metadata (MySQL)
        InvoiceMetadata metadata = InvoiceMetadata.builder()
                .merchant(merchant)
                .clientRequestId(requestId)
                .providerCode(publicRequest.getProviderCode())
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
            // 5. Mapping Public DTO sang Internal Provider Request
            com.einvoicehub.core.provider.model.InvoiceRequest internalRequest = mapToInternalRequest(publicRequest);

            // 6. Gọi Provider Adapter
            InvoiceProvider provider = getProvider(publicRequest.getProviderCode());
            ProviderConfig config = ProviderConfig.builder().build(); // TODO: Lấy config từ MerchantProviderConfig

            com.einvoicehub.core.provider.model.InvoiceResponse internalRes = provider.issueInvoice(internalRequest, config);

            // 7. Xử lý kết quả & Mapping ngược lại Public Response
            if (internalRes.isSuccessful()) {
                handleSuccess(metadata, internalRes, merchant, requestId);
                return mapToPublicResponse(internalRes, true, "Hóa đơn đã được tạo thành công");
            } else {
                handleFailure(metadata, internalRes.getErrorMessage(), internalRes.getErrorCode(), requestId);
                return mapToPublicResponse(internalRes, false, internalRes.getErrorMessage());
            }

        } catch (Exception e) {
            log.error("Lỗi phát hành hóa đơn: {}", e.getMessage());
            handleFailure(metadata, e.getMessage(), "SYSTEM_ERROR", requestId);
            throw e;
        }
    }

    /**
     * Mapping từ Public DTO sang Internal Model để Adapter làm việc.
     */
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
                    item.normalizeData(); // CHUẨN HÓA DỮ LIỆU TRƯỚC KHI MAP
                    return com.einvoicehub.core.provider.model.InvoiceRequest.InvoiceItem.builder()
                            .itemName(item.getItemName())
                            .quantity(item.getQuantity())
                            .unitPrice(item.getUnitPrice())
                            .amount(item.getTotalAmount())
                            .taxRate(item.getTaxRate())
                            .taxAmount(item.getTotalTaxAmount())
                            .description(item.getDescription())
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
        if (provider == null) throw new RuntimeException("Provider not supported: " + code);
        return provider;
    }
}