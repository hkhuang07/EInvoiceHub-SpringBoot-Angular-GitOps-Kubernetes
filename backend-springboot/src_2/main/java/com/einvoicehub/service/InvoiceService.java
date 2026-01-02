package com.einvoicehub.service;

import com.einvoicehub.adapter.InvoiceProvider;
import com.einvoicehub.adapter.InvoiceProviderFactory;
import com.einvoicehub.entity.InvoiceMetadata;
import com.einvoicehub.entity.InvoiceStatus;
import com.einvoicehub.entity.Merchant;
import com.einvoicehub.model.request.InvoiceRequest;
import com.einvoicehub.model.response.InvoiceResponse;
import com.einvoicehub.repository.InvoiceMetadataRepository;
import com.einvoicehub.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Invoice Service - Core Business Logic Layer
 * 
 * Điều phối luồng xuất hóa đơn:
 * 1. Nhận request từ Controller
 * 2. Lưu payload vào MongoDB
 * 3. Chọn Adapter phù hợp và gọi Provider
 * 4. Lưu/Update metadata vào MySQL
 * 5. Trả kết quả về Client
 * 
 * Sử dụng Virtual Threads để xử lý concurrent API calls
 */
@Service
public class InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);
    private static final int MAX_RETRY_COUNT = 3;

    private final InvoiceMetadataRepository invoiceMetadataRepository;
    private final InvoiceProviderFactory providerFactory;
    private final InvoicePayloadService payloadService;
    private final IdGenerator idGenerator;

    public InvoiceService(InvoiceMetadataRepository invoiceMetadataRepository,
                         InvoiceProviderFactory providerFactory,
                         InvoicePayloadService payloadService,
                         IdGenerator idGenerator) {
        this.invoiceMetadataRepository = invoiceMetadataRepository;
        this.providerFactory = providerFactory;
        this.payloadService = payloadService;
        this.idGenerator = idGenerator;
    }

    /**
     * Phát hành hóa đơn mới
     * 
     * @param request Yêu cầu xuất hóa đơn từ client
     * @param merchant Merchant đang thực hiện yêu cầu
     * @return InvoiceResponse Kết quả từ provider
     */
    @Transactional
    public InvoiceResponse issueInvoice(InvoiceRequest request, Merchant merchant) {
        String clientRequestId = idGenerator.generateClientRequestId();
        logger.info("Processing invoice request for merchant: {}, clientRequestId: {}", 
            merchant.getCompanyName(), clientRequestId);

        try {
            // Bước 1: Tạo và lưu metadata với trạng thái PENDING
            InvoiceMetadata metadata = createInitialMetadata(request, merchant, clientRequestId);
            invoiceMetadataRepository.save(metadata);

            // Bước 2: Lưu payload vào MongoDB
            String payloadId = payloadService.saveInvoicePayload(clientRequestId, request);
            metadata.setPayloadId(payloadId);
            invoiceMetadataRepository.save(metadata);

            // Bước 3: Cập nhật trạng thái sang PROCESSING
            metadata.setStatus(InvoiceStatus.PROCESSING);
            metadata.setStatusMessage("Đang xử lý bởi provider");
            invoiceMetadataRepository.save(metadata);

            // Bước 4: Lấy Adapter phù hợp và gọi Provider
            InvoiceProvider provider = providerFactory.getProvider(request.getProviderCode());
            if (provider == null) {
                throw new IllegalArgumentException("Provider không được hỗ trợ: " + request.getProviderCode());
            }

            // Gọi provider (sử dụng Virtual Thread trong provider implementation)
            InvoiceResponse response = provider.issueInvoice(request, metadata);

            // Bước 5: Cập nhật metadata với kết quả
            updateMetadataWithResponse(metadata, response);

            // Bước 6: Cập nhật quota của merchant
            merchant.incrementQuotaUsage();
            metadata.setStatus(response.isSuccess() ? InvoiceStatus.ISSUED : InvoiceStatus.FAILED);

            logger.info("Invoice issued successfully. clientRequestId: {}, invoiceNumber: {}", 
                clientRequestId, response.getInvoiceNumber());

            return response;

        } catch (Exception e) {
            logger.error("Error issuing invoice. clientRequestId: {}", clientRequestId, e);
            
            // Lưu thông tin lỗi vào metadata
            InvoiceMetadata errorMetadata = invoiceMetadataRepository
                .findByClientRequestId(clientRequestId)
                .orElse(null);
            
            if (errorMetadata != null) {
                errorMetadata.setStatus(InvoiceStatus.ERROR);
                errorMetadata.setErrorCode("SYSTEM_ERROR");
                errorMetadata.setErrorMessage(e.getMessage());
                errorMetadata.incrementRetryCount();
                invoiceMetadataRepository.save(errorMetadata);
            }

            return InvoiceResponse.builder()
                .success(false)
                .errorCode("SYSTEM_ERROR")
                .errorMessage("Có lỗi xảy ra trong quá trình xử lý: " + e.getMessage())
                .clientRequestId(clientRequestId)
                .providerCode(request.getProviderCode())
                .build();
        }
    }

    /**
     * Phát hành hóa đơn bất đồng bộ (sử dụng Virtual Thread)
     */
    @Async
    public CompletableFuture<InvoiceResponse> issueInvoiceAsync(InvoiceRequest request, Merchant merchant) {
        return CompletableFuture.completedFuture(issueInvoice(request, merchant));
    }

    /**
     * Lấy trạng thái hóa đơn
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceResponse> getInvoiceStatus(String clientRequestId, Merchant merchant) {
        Optional<InvoiceMetadata> metadataOpt = invoiceMetadataRepository.findByClientRequestId(clientRequestId);
        
        if (metadataOpt.isEmpty()) {
            return Optional.empty();
        }

        InvoiceMetadata metadata = metadataOpt.get();
        
        // Kiểm tra quyền truy cập
        if (!metadata.getMerchantId().equals(merchant.getId())) {
            throw new SecurityException("Không có quyền truy cập hóa đơn này");
        }

        // Nếu hóa đơn đã hoàn thành, trả về từ cache/database
        if (metadata.getStatus().isTerminal()) {
            return Optional.of(buildResponseFromMetadata(metadata));
        }

        // Nếu hóa đơn đang xử lý, gọi provider để kiểm tra trạng thái
        try {
            InvoiceProvider provider = providerFactory.getProvider(metadata.getProviderCode());
            if (provider != null) {
                InvoiceResponse response = provider.getInvoiceStatus(metadata.getProviderTransactionId(), metadata);
                return Optional.of(response);
            }
        } catch (Exception e) {
            logger.warn("Error getting invoice status from provider. clientRequestId: {}", clientRequestId, e);
        }

        // Trả về trạng thái từ metadata
        return Optional.of(buildResponseFromMetadata(metadata));
    }

    /**
     * Lấy danh sách hóa đơn của merchant
     */
    @Transactional(readOnly = true)
    public Page<InvoiceMetadata> getInvoices(Merchant merchant, Pageable pageable) {
        return invoiceMetadataRepository.findByMerchantId(merchant.getId(), pageable);
    }

    /**
     * Lấy danh sách hóa đơn theo trạng thái
     */
    @Transactional(readOnly = true)
    public Page<InvoiceMetadata> getInvoicesByStatus(Merchant merchant, InvoiceStatus status, Pageable pageable) {
        return invoiceMetadataRepository.findByMerchantIdAndStatus(merchant.getId(), status, pageable);
    }

    /**
     * Lấy chi tiết hóa đơn
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceMetadata> getInvoiceDetail(String clientRequestId, Merchant merchant) {
        Optional<InvoiceMetadata> metadataOpt = invoiceMetadataRepository.findByClientRequestId(clientRequestId);
        
        if (metadataOpt.isPresent() && metadataOpt.get().getMerchantId().equals(merchant.getId())) {
            return metadataOpt;
        }
        
        return Optional.empty();
    }

    /**
     * Hủy hóa đơn
     */
    @Transactional
    public InvoiceResponse cancelInvoice(String clientRequestId, Merchant merchant, String reason) {
        Optional<InvoiceMetadata> metadataOpt = invoiceMetadataRepository.findByClientRequestId(clientRequestId);
        
        if (metadataOpt.isEmpty()) {
            return InvoiceResponse.builder()
                .success(false)
                .errorCode("NOT_FOUND")
                .errorMessage("Không tìm thấy hóa đơn")
                .build();
        }

        InvoiceMetadata metadata = metadataOpt.get();
        
        // Kiểm tra quyền
        if (!metadata.getMerchantId().equals(merchant.getId())) {
            return InvoiceResponse.builder()
                .success(false)
                .errorCode("FORBIDDEN")
                .errorMessage("Không có quyền hủy hóa đơn này")
                .build();
        }

        // Kiểm tra trạng thái có thể hủy
        if (!metadata.getStatus().isCancellable()) {
            return InvoiceResponse.builder()
                .success(false)
                .errorCode("INVALID_STATUS")
                .errorMessage("Hóa đơn ở trạng thái không thể hủy: " + metadata.getStatus().getDisplayName())
                .build();
        }

        try {
            // Gọi provider để hủy
            InvoiceProvider provider = providerFactory.getProvider(metadata.getProviderCode());
            if (provider != null) {
                InvoiceResponse response = provider.cancelInvoice(metadata.getProviderTransactionId(), reason, metadata);
                
                if (response.isSuccess()) {
                    metadata.setStatus(InvoiceStatus.CANCELLED);
                    metadata.setStatusMessage("Đã hủy: " + reason);
                    metadata.setCancelledDate(LocalDateTime.now());
                    invoiceMetadataRepository.save(metadata);
                    
                    // Hoàn lại quota
                    merchant.decrementQuotaUsage();
                }
                
                return response;
            }
        } catch (Exception e) {
            logger.error("Error cancelling invoice. clientRequestId: {}", clientRequestId, e);
        }

        return InvoiceResponse.builder()
            .success(false)
            .errorCode("CANCEL_FAILED")
            .errorMessage("Không thể hủy hóa đơn")
            .build();
    }

    /**
     * Tạo metadata ban đầu
     */
    private InvoiceMetadata createInitialMetadata(InvoiceRequest request, Merchant merchant, String clientRequestId) {
        InvoiceMetadata metadata = new InvoiceMetadata();
        metadata.setMerchantId(merchant.getId());
        metadata.setClientRequestId(clientRequestId);
        metadata.setProviderCode(request.getProviderCode());
        metadata.setStatus(InvoiceStatus.PENDING);
        metadata.setStatusMessage("Đang chờ xử lý");
        
        // Thông tin tài chính
        metadata.setTotalAmount(request.getTotalAmount() != null ? request.getTotalAmount() : BigDecimal.ZERO);
        metadata.setTotalTaxAmount(request.getTotalTaxAmount() != null ? request.getTotalTaxAmount() : BigDecimal.ZERO);
        metadata.setGrandTotalAmount(request.getGrandTotalAmount() != null ? request.getGrandTotalAmount() : BigDecimal.ZERO);
        metadata.setCurrency(request.getCurrency() != null ? request.getCurrency() : "VND");
        
        // Thông tin bên
        metadata.setSellerName(request.getSellerName());
        metadata.setSellerTaxCode(request.getSellerTaxCode());
        metadata.setBuyerName(request.getBuyerName());
        metadata.setBuyerTaxCode(request.getBuyerTaxCode());
        
        // Ngày hóa đơn
        metadata.setInvoiceDate(request.getInvoiceDate());
        
        return metadata;
    }

    /**
     * Cập nhật metadata với response từ provider
     */
    private void updateMetadataWithResponse(InvoiceMetadata metadata, InvoiceResponse response) {
        if (response.isSuccess()) {
            metadata.setStatus(InvoiceStatus.ISSUED);
            metadata.setInvoiceNumber(response.getInvoiceNumber());
            metadata.setInvoiceCode(response.getInvoiceCode());
            metadata.setInvoicePattern(response.getInvoicePattern());
            metadata.setInvoiceSerial(response.getInvoiceSerial());
            metadata.setProviderTransactionId(response.getTransactionId());
            metadata.setIssuedDate(response.getIssuedDate());
            metadata.setSignedDate(response.getSignedDate());
            metadata.setPdfUrl(response.getPdfDownloadUrl());
            metadata.setXmlUrl(response.getXmlDownloadUrl());
            metadata.setHtmlUrl(response.getHtmlDownloadUrl());
            metadata.setStatusMessage("Phát hành thành công");
        } else {
            metadata.setStatus(InvoiceStatus.FAILED);
            metadata.setErrorCode(response.getErrorCode());
            metadata.setErrorMessage(response.getErrorMessage());
            metadata.setStatusMessage("Phát hành thất bại: " + response.getErrorMessage());
        }
        
        metadata.setCompletedAt(LocalDateTime.now());
    }

    /**
     * Build response từ metadata
     */
    private InvoiceResponse buildResponseFromMetadata(InvoiceMetadata metadata) {
        return InvoiceResponse.builder()
            .success(metadata.getStatus().isSuccess())
            .errorCode(metadata.getErrorCode())
            .errorMessage(metadata.getErrorMessage())
            .providerCode(metadata.getProviderCode())
            .transactionId(metadata.getProviderTransactionId())
            .clientRequestId(metadata.getClientRequestId())
            .invoiceNumber(metadata.getInvoiceNumber())
            .status(metadata.getStatus().name())
            .totalAmount(metadata.getTotalAmount())
            .grandTotalAmount(metadata.getGrandTotalAmount())
            .buyerName(metadata.getBuyerName())
            .buyerTaxCode(metadata.getBuyerTaxCode())
            .sellerName(metadata.getSellerName())
            .sellerTaxCode(metadata.getSellerTaxCode())
            .build();
    }
}