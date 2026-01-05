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


/*

File mơới đucợ minimax viếtpackage com.einvoicehub.core.service;

import com.einvoicehub.core.adapter.InvoiceProvider;
import com.einvoicehub.core.common.exception.AppException;
import com.einvoicehub.core.common.exception.ErrorCode;
import com.einvoicehub.core.dto.InvoiceRequest;
import com.einvoicehub.core.dto.InvoiceResponse;
import com.einvoicehub.core.entity.InvoiceMetadata;
import com.einvoicehub.core.entity.Merchant;
import com.einvoicehub.core.repository.InvoiceMetadataRepository;
import com.einvoicehub.core.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service điều phối quy trình tạo hóa đơn điện tử.
 * Ví dụ minh họa cách sử dụng hệ thống exception management.
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
     * Ví dụ 1: Sử dụng AppException với ErrorCode đơn giản.
     * Phù hợp cho các trường hợp lỗi đã được định nghĩa sẵn.
     */
    public Merchant validateMerchant(String merchantCode) {
        return merchantRepository.findByCode(merchantCode)
                .orElseThrow(() -> new AppException(ErrorCode.MERCHANT_NOT_FOUND));
    }

    /**
     * Ví dụ 2: Sử dụng AppException với message tùy chỉnh.
     * Phù hợp khi cần thông báo chi tiết hơn.
     */
    public void checkQuota(Merchant merchant) {
        if (merchant.getCurrentInvoiceCount() >= merchant.getInvoiceQuota()) {
            throw new AppException(
                    ErrorCode.INSUFFICIENT_QUOTA,
                    String.format("Merchant %s đã sử dụng %d/%d hóa đơn. " +
                                    "Vui lòng nâng cấp gói để tiếp tục.",
                            merchant.getCode(),
                            merchant.getCurrentInvoiceCount(),
                            merchant.getInvoiceQuota())
            );
        }
    }

    /**
     * Ví dụ 3: Sử dụng AppException với cause exception.
     * Phù hợp khi cần chain exception để debug.
     */
    public InvoiceProvider getProvider(String providerCode) {
        String normalizedCode = providerCode.toUpperCase();
        InvoiceProvider provider = invoiceProviderMap.get(normalizedCode);

        if (provider == null) {
            throw new AppException(
                    ErrorCode.PROVIDER_ERROR,
                    "Provider không được hỗ trợ: " + providerCode,
                    new IllegalArgumentException("Unknown provider: " + providerCode)
            );
        }
        return provider;
    }

    /**
     * Ví dụ 4: Workflow hoàn chỉnh với exception handling.
     * Demo cách sử dụng tất cả các pattern trong một method.
     */
    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        String transactionId = UUID.randomUUID().toString();
        log.info("Bắt đầu tạo hóa đơn với transactionId: {}", transactionId);

        // Bước 1: Validate merchant
        Merchant merchant = validateMerchant(request.getMerchantId());

        // Bước 2: Check quota
        checkQuota(merchant);

        // Bước 3: Lấy provider
        InvoiceProvider provider = getProvider(request.getProviderCode());

        // Bước 4: Create InvoiceMetadata với trạng thái PENDING
        InvoiceMetadata metadata = InvoiceMetadata.builder()
                .transactionId(transactionId)
                .merchantCode(request.getMerchantId())
                .providerCode(request.getProviderCode())
                .status("PENDING")
                .build();
        invoiceMetadataRepository.save(metadata);

        try {
            // Bước 5: Gọi provider
            InvoiceResponse response = provider.createInvoice(request);

            // Bước 6: Update thành công
            metadata.setStatus("SUCCESS");
            metadata.setProviderInvoiceId(response.getInvoiceId());
            invoiceMetadataRepository.save(metadata);

            // Bước 7: Increment usage
            merchant.setCurrentInvoiceCount(merchant.getCurrentInvoiceCount() + 1);
            merchantRepository.save(merchant);

            log.info("Tạo hóa đơn thành công: transactionId={}, invoiceId={}",
                    transactionId, response.getInvoiceId());

            return response;

        } catch (Exception e) {
            // Log exception với context
            log.error("Lỗi khi tạo hóa đơn cho transactionId {}: {}",
                    transactionId, e.getMessage(), e);

            // Update thất bại
            metadata.setStatus("FAILED");
            metadata.setErrorMessage(e.getMessage());
            invoiceMetadataRepository.save(metadata);

            // Wrap và rethrow để GlobalExceptionHandler xử lý
            throw new AppException(
                    ErrorCode.PROVIDER_ERROR,
                    "Lỗi khi tạo hóa đơn: " + e.getMessage(),
                    e
            );
        }
    }
}
 */