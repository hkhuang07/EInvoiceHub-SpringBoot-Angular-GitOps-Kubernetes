package com.einvoicehub.core.service;

import com.einvoicehub.core.domain.entity.*;
import com.einvoicehub.core.domain.repository.*;
import com.einvoicehub.core.dto.request.SubmitInvoice.SubmitInvoiceRequest;
import com.einvoicehub.core.dto.response.EinvInvoiceMetadataResponse;
import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.mapper.EinvHubMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvInvoiceMetadataService {

    private final EinvInvoiceMetadataRepository repository;
    private final EinvMerchantRepository merchantRepository;
    private final EinvInvoiceTemplateRepository templateRepository;
    private final EinvInvoiceStatusRepository statusRepository;
    private final EinvInvoiceItemRepository itemRepository;
    private final EinvInvoiceAdjustmentRepository adjustmentRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvInvoiceMetadataResponse> getAll() {
        log.info("[Transaction] Đang lấy danh sách toàn bộ hóa đơn");
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvInvoiceMetadataResponse getById(Long id) {
        log.info("[Transaction] Tìm kiếm hóa đơn ID: {}", id);
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Hóa đơn không tồn tại"));
    }

    @Transactional
    public EinvInvoiceMetadataResponse create(SubmitInvoiceRequest request) {
        log.info("[Transaction] Bắt đầu tạo hóa đơn cho Merchant ID: {}", request.getInvoiceTypeId());

        EinvInvoiceTemplateEntity template = templateRepository.findById(request.getInvoiceTypeId())
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Mẫu hóa đơn không tồn tại"));

        MerchantEntity merchant = template.getMerchant();
        // Requirement 4: Kiểm tra trạng thái hoạt động của Merchant
        if (Boolean.TRUE.equals(merchant.getIsDeleted())) {
            log.error("[Transaction] Merchant {} đã ngừng hoạt động", merchant.getTaxCode());
            throw new InvalidDataException(ErrorCode.MERCHANT_NOT_FOUND, "Doanh nghiệp đã ngừng hoạt động trên hệ thống");
        }

        synchronized (this) {
            Integer nextNumber = template.getCurrentNumber() + 1;
            if (nextNumber > template.getMaxNumber()) {
                log.error("[Transaction] Mẫu {} đã sử dụng hết dải số (%d/%d)",
                        template.getSymbolCode(), nextNumber, template.getMaxNumber());
                throw new InvalidDataException(ErrorCode.INVALID_DATA, "Dải số hóa đơn đã hết, vui lòng thông báo phát hành dải số mới");
            }

            template.setCurrentNumber(nextNumber);
            templateRepository.save(template);

            EinvInvoiceEntity entity = mapper.toEntity(request);
            entity.setMerchant(merchant);
            entity.setInvoiceTemplate(template);
            entity.setInvoiceNumber(String.format("%08d", nextNumber)); // Format 8 chữ số chuẩn CQT
            entity.setSymbolCode(template.getSymbolCode());
            entity.setTemplateCode(template.getTemplateCode());

            statusRepository.findById(1).ifPresent(entity::setInvoiceStatus);

            entity.setIsDeleted(false);
            entity = repository.save(entity);

            log.info("[Transaction] Đã tạo thành công hóa đơn số: {} (ID: {})", entity.getInvoiceNumber(), entity.getId());
            return mapper.toResponse(entity);
        }
    }

    @Transactional
    public EinvInvoiceMetadataResponse update(Long id, SubmitInvoiceRequest request) {
        log.info("[Transaction] Cập nhật thông tin hóa đơn ID: {}", id);
        EinvInvoiceEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Không tìm thấy hóa đơn"));

        if (entity.getInvoiceStatus().getId() != 1) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Hóa đơn đã chốt trạng thái, không thể chỉnh sửa");
        }

        entity.setBuyerFullName(request.getBuyerName());
        entity.setBuyerEmail(request.getReceiverEmail());
        entity.setBuyerAddress(request.getBuyerAddress());
        entity.setBuyerTaxCode(request.getBuyerTaxCode());
        entity.setCurrencyCode(request.getCurrencyCode());
        entity.setExchangeRate(request.getExchangeRate());

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.warn("[Transaction] Đang thực hiện xóa mềm hóa đơn ID: {}", id);
        EinvInvoiceEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Hóa đơn không tồn tại"));

        if (entity.getInvoiceStatus().getId() != 1) {
            log.error("[Transaction] Xóa mềm thất bại: Hóa đơn ID {} đã chốt trạng thái", id);
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Chỉ được phép xóa hóa đơn ở trạng thái Nháp");
        }
        if (adjustmentRepository.existsByOriginalInvoiceId(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Hóa đơn này đã có biên bản liên quan, không thể xóa");
        }

        entity.setIsDeleted(true);
        entity.setDeletedAt(java.time.LocalDateTime.now());
        // entity.setDeletedByUser(currentUser); // Nếu có SecurityContext
        repository.save(entity);
        log.info("[Transaction] Đã xóa mềm thành công hóa đơn ID: {}", id);
    }

    @Transactional
    public void hardDelete(Long id) {
        log.warn("[Transaction] CẢNH BÁO: Thực hiện xóa cứng hóa đơn ID: {}", id);
        EinvInvoiceEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại trong hệ thống"));

        if (entity.getInvoiceStatus().getId() != 1) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Lệnh xóa cứng bị chặn: Hóa đơn không phải trạng thái Nháp");
        }

        if (adjustmentRepository.existsByOriginalInvoiceId(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Vi phạm ràng buộc: Hóa đơn đang có biên bản thay thế/điều chỉnh");
        }
        itemRepository.deleteByInvoiceId(id);
        log.debug("[Transaction] Đã dọn dẹp chi tiết hàng hóa của hóa đơn ID: {}", id);
        repository.delete(entity);
        log.info("[Transaction] Đã xóa vĩnh viễn hóa đơn ID: {} khỏi Database", id);
    }
}