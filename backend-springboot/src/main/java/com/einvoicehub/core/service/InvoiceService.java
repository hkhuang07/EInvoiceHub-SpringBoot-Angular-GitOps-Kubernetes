package com.einvoicehub.core.service;

import com.einvoicehub.core.common.exception.AppException;
import com.einvoicehub.core.common.exception.ErrorCode;
import com.einvoicehub.core.dto.request.InvoiceItemRequest;
import com.einvoicehub.core.dto.request.InvoiceRequest;
import com.einvoicehub.core.dto.response.InvoiceResponse;
import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.entity.jpa.*;
import com.einvoicehub.core.provider.InvoiceProvider;
import com.einvoicehub.core.provider.ProviderConfig;
import com.einvoicehub.core.repository.jpa.InvoiceItemRepository;
import com.einvoicehub.core.repository.jpa.InvoiceMetadataRepository;
import com.einvoicehub.core.repository.jpa.MerchantProviderConfigRepository;
import com.einvoicehub.core.repository.jpa.MerchantRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final MerchantRepository merchantRepository;
    private final InvoiceMetadataRepository invoiceMetadataRepository;
    private final MerchantProviderConfigRepository providerConfigRepository;
    private final InvoicePayloadService invoicePayloadService;
    private final InvoiceItemRepository invoiceItemRepository;
    private final Map<String, InvoiceProvider> invoiceProviders;
    private final ObjectMapper objectMapper;

    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        String requestId = request.getInternalReferenceCode();
        log.info("Starting professional invoice issuance: {}", requestId);

        // 1. Kiểm tra Merchant & Quota
        Merchant merchant = merchantRepository.findById(Long.parseLong(request.getMerchantId()))
                .orElseThrow(() -> new AppException(ErrorCode.MERCHANT_NOT_FOUND));

        if (!merchant.hasAvailableQuota()) {
            throw new AppException(ErrorCode.INSUFFICIENT_QUOTA);
        }

        // 2. Lấy cấu hình kết nối
        MerchantProviderConfig merchantConfig = providerConfigRepository
                .findByMerchantIdAndProviderProviderCode(merchant.getId(), request.getProviderCode())
                .orElseThrow(() -> new AppException(ErrorCode.VALIDATION_ERROR, "Config Provider not found"));

        // 3. Khởi tạo Metadata & Payload
        InvoiceMetadata metadata = initMetadata(merchant, request, requestId, merchantConfig);
        saveInitialPayload(merchant.getId(), requestId, request.getProviderCode(), request);

        // 4. Lưu chi tiết hàng hóa (MariaDB)
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            saveInvoiceItems(metadata.getId(), request.getItems());
        }

        try {
            InvoiceProvider provider = getProvider(request.getProviderCode());
            ProviderConfig config = mapToProviderConfig(merchantConfig);
            var internalRequest = mapToInternalRequest(request, metadata.getId());

            // 5. Gọi API Provider qua Virtual Thread
            var internalRes = virtualThreadExecutor.submit(() -> provider.issueInvoice(internalRequest, config)).join();

            if (internalRes.isSuccessful()) {
                handleSuccess(metadata, requestId, internalRes, merchant);
                return mapToPublicResponse(internalRes, metadata, true, "Hóa đơn đã phát hành thành công");
            } else {
                handleFailure(metadata, requestId, internalRes.getErrorMessage(), internalRes.getErrorCode());
                return mapToPublicResponse(internalRes, metadata, false, internalRes.getErrorMessage());
            }

        } catch (Exception e) {
            log.error("Critical error during issuance for requestId {}: {}", requestId, e.getMessage());
            handleFailure(metadata, requestId, e.getMessage(), "SYSTEM_ERROR");
            throw new AppException(ErrorCode.PROVIDER_ERROR, "Provider connection failed: " + e.getMessage());
        }
    }

    private void saveInitialPayload(Long merchantId, String clientRequestId, String providerCode, InvoiceRequest request) {
        Map<String, Object> requestData = objectMapper.convertValue(request, new TypeReference<Map<String, Object>>() {});
        invoicePayloadService.createPayloadFromRequest(merchantId, clientRequestId, providerCode, requestData);
    }

    private void handleSuccess(InvoiceMetadata metadata, String requestId, com.einvoicehub.core.provider.model.InvoiceResponse res, Merchant merchant) {
        metadata.markAsSuccess(res.getTransactionCode());
        metadata.setInvoiceNumber(res.getInvoiceNumber());
        metadata.setCqtCode(res.getCqtCode());
        metadata.setSignedAt(LocalDateTime.now());

        // Cập nhật lại dải hóa đơn nếu provider trả về khác với dải gửi đi
        if (res.getTemplateCode() != null) metadata.setTemplateCode(res.getTemplateCode());
        if (res.getSymbolCode() != null) metadata.setSymbolCode(res.getSymbolCode());

        invoiceMetadataRepository.save(metadata);

        // Đồng bộ Payload chuyên sâu
        invoicePayloadService.updateInvoiceId(requestId, metadata.getId());
        if (res.getXmlUrl() != null) invoicePayloadService.updateXmlContent(requestId, res.getXmlUrl());
        if (res.getJsonUrl() != null) invoicePayloadService.updateJsonContent(requestId, res.getJsonUrl());
        invoicePayloadService.updatePayloadWithResponse(requestId, res.toString(), "SUCCESS");

        // Trừ hạn mức
        merchant.incrementInvoiceUsage();
        merchantRepository.save(merchant);
    }

    private void handleFailure(InvoiceMetadata metadata, String requestId, String errorMessage, String errorCode) {
        metadata.markAsFailed(errorCode, errorMessage);
        invoiceMetadataRepository.save(metadata);
        invoicePayloadService.updatePayloadWithResponse(requestId, errorMessage, "FAILED");
    }

    private InvoiceResponse mapToPublicResponse(com.einvoicehub.core.provider.model.InvoiceResponse res, InvoiceMetadata meta, boolean success, String msg) {
        return InvoiceResponse.builder()
                .success(success)
                .transactionId(res.getTransactionCode())
                .invoiceNumber(res.getInvoiceNumber())
                .cqtCode(res.getCqtCode())
                .clientRequestId(meta.getClientRequestId())
                .providerCode(meta.getProviderCode())
                .status(meta.getStatus().name())
                .buyerName(meta.getBuyerName())
                .totalAmount(meta.getTotalAmount())
                .currency(meta.getCurrencyCode())
                .pdfDownloadUrl(res.getPdfUrl())
                .xmlDownloadUrl(res.getXmlUrl())
                .message(msg)
                .errorCode(res.getErrorCode())
                .build();
    }

    private InvoiceProvider getProvider(String code) {
        InvoiceProvider p = invoiceProviders.get(code.toUpperCase());
        if (p == null) throw new AppException(ErrorCode.PROVIDER_ERROR, "Provider not supported: " + code);
        return p;
    }

    private ProviderConfig mapToProviderConfig(MerchantProviderConfig e) {
        return ProviderConfig.builder()
                .configId(e.getId())
                .providerCode(e.getProviderCode())
                .username(e.getUsernameService())
                .password(e.getPasswordServiceEncrypted())
                .certificateData(e.getCertificateData())
                .certificateSerial(e.getCertificateSerial())
                .extraConfigJson(e.getExtraConfig())
                .build();
    }

    private InvoiceMetadata initMetadata(Merchant merchant, InvoiceRequest request, String requestId, MerchantProviderConfig config) {
        return invoiceMetadataRepository.save(InvoiceMetadata.builder()
                .merchant(merchant)
                .provider(config.getProvider())
                .providerConfig(config)
                .clientRequestId(requestId)
                .providerCode(request.getProviderCode())
                .invoiceTypeCode(request.getInvoiceType())
                .templateCode(request.getTemplateCode())
                .symbolCode(request.getSymbolCode())
                .sellerName(request.getSellerName())
                .sellerTaxCode(request.getSellerTaxCode())
                .sellerAddress(request.getSellerAddress())
                .buyerName(request.getBuyerName())
                .buyerTaxCode(request.getBuyerTaxCode())
                .buyerEmail(request.getBuyerEmail())
                .buyerAddress(request.getBuyerAddress())
                .subtotalAmount(calculateSubtotal(request.getItems()))
                .taxAmount(request.getTotalTaxAmount())
                .totalAmount(request.getGrandTotalAmount())
                .currencyCode(request.getCurrency() != null ? request.getCurrency() : "VND")
                .issueDate(request.getInvoiceDate() != null ? request.getInvoiceDate().toLocalDate() : LocalDate.now())
                .status(InvoiceStatus.PENDING)
                .build());
    }

    private void saveInvoiceItems(Long invoiceId, List<InvoiceItemRequest> itemRequests) {
        List<InvoiceItem> items = itemRequests.stream().map(req -> {
            req.normalizeData();
            return InvoiceItem.builder()
                    .invoiceId(invoiceId)
                    .productName(req.getItemName())
                    .quantity(req.getQuantity())
                    .unitPrice(req.getUnitPrice())
                    .amount(req.getAmount())
                    .taxRate(req.getTaxRate())
                    .totalAmount(req.getTotalAmountWithVat())
                    .build();
        }).toList();
        invoiceItemRepository.saveAll(items);
    }

    private com.einvoicehub.core.provider.model.InvoiceRequest mapToInternalRequest(InvoiceRequest pub, Long metadataId) {
        return com.einvoicehub.core.provider.model.InvoiceRequest.builder()
                .clientRequestId(pub.getInternalReferenceCode())
                .invoiceMetadataId(metadataId)
                .providerCode(pub.getProviderCode())
                .invoiceType(pub.getInvoiceType())
                .templateCode(pub.getTemplateCode())
                .symbolCode(pub.getSymbolCode())
                .issueDate(pub.getInvoiceDate() != null ? pub.getInvoiceDate().toLocalDate() : LocalDate.now())
                .seller(com.einvoicehub.core.provider.model.InvoiceRequest.SellerInfo.builder()
                        .name(pub.getSellerName()).taxCode(pub.getSellerTaxCode()).address(pub.getSellerAddress()).build())
                .buyer(com.einvoicehub.core.provider.model.InvoiceRequest.BuyerInfo.builder()
                        .name(pub.getBuyerName()).taxCode(pub.getBuyerTaxCode()).address(pub.getBuyerAddress()).build())
                .items(pub.getItems() == null ? new ArrayList<>() : pub.getItems().stream().map(i ->
                        com.einvoicehub.core.provider.model.InvoiceRequest.InvoiceItem.builder()
                                .itemName(i.getItemName()).quantity(i.getQuantity()).unitPrice(i.getUnitPrice())
                                .amount(i.getAmount()).taxRate(i.getTaxRate()).build()).toList())
                .summary(com.einvoicehub.core.provider.model.InvoiceRequest.InvoiceSummary.builder()
                        .totalAmount(pub.getGrandTotalAmount())
                        .totalTaxAmount(pub.getTotalTaxAmount())
                        .currencyCode(pub.getCurrency()).build())
                .build();
    }

    private BigDecimal calculateSubtotal(List<InvoiceItemRequest> items) {
        return items == null ? BigDecimal.ZERO : items.stream()
                .map(i -> i.getAmount() != null ? i.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}