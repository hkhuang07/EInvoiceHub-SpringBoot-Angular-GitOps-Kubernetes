package com.einvoicehub.core.service;

import com.einvoicehub.core.common.exception.AppException;
import com.einvoicehub.core.common.exception.ErrorCode;
import com.einvoicehub.core.dto.request.InvoiceRequest;
import com.einvoicehub.core.dto.response.InvoiceResponse;
import com.einvoicehub.core.entity.enums.InvoiceStatus;
import com.einvoicehub.core.entity.jpa.InvoicePayload;
import com.einvoicehub.core.entity.jpa.InvoiceMetadata;
import com.einvoicehub.core.entity.jpa.Merchant;
import com.einvoicehub.core.entity.jpa.MerchantProviderConfig;
import com.einvoicehub.core.provider.InvoiceProvider;
import com.einvoicehub.core.provider.ProviderConfig;
import com.einvoicehub.core.repository.jpa.InvoiceMetadataRepository;
import com.einvoicehub.core.repository.jpa.MerchantProviderConfigRepository;
import com.einvoicehub.core.repository.jpa.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final MerchantRepository merchantRepo;
    private final InvoiceMetadataRepository invoiceMetadataRepo;
    private final MerchantProviderConfigRepository providerConfigRepo;
    private final InvoicePayloadService invoicePayloadService;
    private final Map<String, InvoiceProvider> providers;

    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        String requestId = request.getInternalReferenceCode();
        log.info("Starting invoice issuance process. RequestID: {}", requestId);

        Merchant merchant = merchantRepo.findById(Long.parseLong(request.getMerchantId()))
                .orElseThrow(() -> new AppException(ErrorCode.MERCHANT_NOT_FOUND));

        if (!merchant.hasAvailableQuota()) {
            throw new AppException(ErrorCode.INSUFFICIENT_QUOTA, "Merchant quota exceeded");
        }

        /* Load Provider Configuration for this specific Merchant */
        MerchantProviderConfig merchantConfig = providerConfigRepo
                .findByMerchantIdAndProviderProviderCode(merchant.getId(), request.getProviderCode())
                .orElseThrow(() -> new AppException(ErrorCode.VALIDATION_ERROR, "Provider config not found"));

        InvoiceMetadata metadata = initMetadata(merchant, request, requestId, merchantConfig);
        saveInitialPayload(merchant, request, requestId);

        try {
            InvoiceProvider provider = getProvider(request.getProviderCode());
            ProviderConfig config = mapToProviderConfig(merchantConfig);

            var internalRequest = mapToInternalRequest(request, metadata.getId());
            var internalRes = provider.issueInvoice(internalRequest, config);

            if (internalRes.isSuccessful()) {
                handleSuccess(metadata, internalRes, merchant, requestId);
                return mapToPublicResponse(internalRes, true, "Invoice issued successfully");
            } else {
                handleFailure(metadata, internalRes.getErrorMessage(), internalRes.getErrorCode(), requestId);
                return mapToPublicResponse(internalRes, false, internalRes.getErrorMessage());
            }

        } catch (Exception e) {
            log.error("Critical system error during issuance: {}", e.getMessage());
            handleFailure(metadata, e.getMessage(), "SYSTEM_ERROR", requestId);
            throw new AppException(ErrorCode.PROVIDER_ERROR, "Provider connection failed: " + e.getMessage(), e);
        }
    }

    private com.einvoicehub.core.provider.model.InvoiceRequest mapToInternalRequest(InvoiceRequest pub, Long metadataId) {
        return com.einvoicehub.core.provider.model.InvoiceRequest.builder()
                .clientRequestId(pub.getInternalReferenceCode())
                .invoiceMetadataId(metadataId)
                .providerCode(pub.getProviderCode())
                .invoiceType(pub.getInvoiceType())
                .issueDate(pub.getInvoiceDate().toLocalDate())
                .seller(com.einvoicehub.core.provider.model.InvoiceRequest.SellerInfo.builder()
                        .name(pub.getSellerName()).taxCode(pub.getSellerTaxCode()).address(pub.getSellerAddress()).build())
                .buyer(com.einvoicehub.core.provider.model.InvoiceRequest.BuyerInfo.builder()
                        .name(pub.getBuyerName()).taxCode(pub.getBuyerTaxCode()).address(pub.getBuyerAddress()).build())
                .items(pub.getItems().stream().map(item ->
                        com.einvoicehub.core.provider.model.InvoiceRequest.InvoiceItem.builder()
                                .itemName(item.getItemName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .amount(item.getAmount())
                                .taxRate(item.getTaxRate())
                                .taxAmount(item.getTaxAmount())
                                .build()
                ).toList())
                .summary(com.einvoicehub.core.provider.model.InvoiceRequest.InvoiceSummary.builder()
                        .totalAmount(pub.getGrandTotalAmount())
                        .totalTaxAmount(pub.getTotalTaxAmount())
                        .currencyCode(pub.getCurrency())
                        .build())
                .build();
    }

    private ProviderConfig mapToProviderConfig(MerchantProviderConfig entity) {
        return ProviderConfig.builder()
                .configId(entity.getId())
                .providerCode(entity.getProviderCode())
                .username(entity.getUsernameService())
                .password(entity.getPasswordServiceEncrypted())
                .certificateData(entity.getCertificateData())
                .certificateSerial(entity.getCertificateSerial())
                .extraConfigJson(entity.getExtraConfig())
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

    private InvoiceMetadata initMetadata(Merchant merchant, InvoiceRequest req, String requestId, MerchantProviderConfig config) {
        return invoiceMetadataRepo.save(InvoiceMetadata.builder()
                .merchant(merchant)
                .provider(config.getProvider())
                .providerConfig(config)
                .clientRequestId(requestId)
                .providerCode(req.getProviderCode())
                .status(InvoiceStatus.PENDING)
                .build());
    }

    private void saveInitialPayload(Merchant merchant, InvoiceRequest req, String requestId) {
        invoicePayloadService.savePayload(InvoicePayload.builder()
                .clientRequestId(requestId)
                .merchantId(merchant.getId())
                .status("PENDING")
                .rawRequest(req)
                .build());
    }

    private void handleSuccess(InvoiceMetadata metadata, com.einvoicehub.core.provider.model.InvoiceResponse res, Merchant merchant, String requestId) {
        metadata.markAsSuccess(res.getTransactionCode());
        metadata.setInvoiceNumber(res.getInvoiceNumber());
        invoiceMetadataRepo.save(metadata);

        invoicePayloadService.updatePayloadWithResponse(requestId, res.toString(), "SUCCESS");

        merchant.incrementInvoiceUsage();
        merchantRepo.save(merchant);
    }

    private void handleFailure(InvoiceMetadata metadata, String error, String errorCode, String requestId) {
        metadata.markAsFailed(errorCode, error);
        invoiceMetadataRepo.save(metadata);
        invoicePayloadService.updatePayloadWithResponse(requestId, error, "FAILED");
    }

    private InvoiceProvider getProvider(String code) {
        InvoiceProvider provider = providers.get(code.toUpperCase());
        if (provider == null) throw new AppException(ErrorCode.PROVIDER_ERROR, "Unsupported provider: " + code);
        return provider;
    }
}