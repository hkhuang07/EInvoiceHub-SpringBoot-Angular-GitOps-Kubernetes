package com.einvoicehub.core.service;

import com.einvoicehub.core.domain.entity.EinvInvoiceAdjustmentsEntity;
import com.einvoicehub.core.domain.entity.EinvInvoiceMetadataEntity;
import com.einvoicehub.core.domain.repository.EinvInvoiceAdjustmentRepository;
import com.einvoicehub.core.domain.repository.EinvInvoiceMetadataRepository;
import com.einvoicehub.core.dto.request.EinvInvoiceAdjustmentRequest;
import com.einvoicehub.core.dto.response.EinvInvoiceAdjustmentResponse;
import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.mapper.EinvHubMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvInvoiceAdjustmentService {

    private final EinvInvoiceAdjustmentRepository repository;
    private final EinvInvoiceMetadataRepository metadataRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvInvoiceAdjustmentResponse> getAll() {
        log.info("[Adjustment] Truy vấn danh sách biên bản điều chỉnh");
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvInvoiceAdjustmentResponse getById(Long id) {
        log.info("[Adjustment] Tìm kiếm biên bản ID: {}", id);
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Không tìm thấy biên bản"));
    }

    @Transactional
    public EinvInvoiceAdjustmentResponse create(EinvInvoiceAdjustmentRequest request) {
        log.info("[Adjustment] Lập biên bản {} cho hóa đơn gốc ID: {}",
                request.getAdjustmentType(), request.getOriginalInvoiceId());
        EinvInvoiceMetadataEntity originalInvoice = metadataRepository.findById(request.getOriginalInvoiceId())
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Hóa đơn gốc không tồn tại"));

        if (originalInvoice.getInvoiceStatus().getId() != 5) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Chỉ được phép lập biên bản cho hóa đơn đã gửi CQT thành công");
        }

        if (Boolean.TRUE.equals(originalInvoice.getMerchant().getIsDeleted())) {
            throw new InvalidDataException(ErrorCode.MERCHANT_NOT_FOUND, "Doanh nghiệp đã bị khóa");
        }

        EinvInvoiceAdjustmentsEntity entity = mapper.toEntity(request);
        entity.setOriginalInvoice(originalInvoice);

        BigDecimal difference = request.getNewTotalAmount().subtract(request.getOldTotalAmount());
        entity.setDifferenceAmount(difference);

        entity = repository.save(entity);
        log.info("[Adjustment] Đã lưu biên bản điều chỉnh ID: {}", entity.getId());
        return mapper.toResponse(entity);
    }

    @Transactional
    public EinvInvoiceAdjustmentResponse update(Long id, EinvInvoiceAdjustmentRequest request) {
        log.info("[Adjustment] Cập nhật biên bản ID: {}", id);
        EinvInvoiceAdjustmentsEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Biên bản không tồn tại"));

        if (!"PENDING".equals(entity.getStatus().name())) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Biên bản đã được duyệt hoặc gửi đi, không thể sửa");
        }

        entity.setAgreementNumber(request.getAgreementNumber());
        entity.setAgreementDate(request.getAgreementDate());
        entity.setReasonDescription(request.getReasonDescription());
        entity.setNewTotalAmount(request.getNewTotalAmount());
        entity.setDifferenceAmount(request.getNewTotalAmount().subtract(entity.getOldTotalAmount()));

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.warn("[Adjustment] Yêu cầu xóa biên bản ID: {}", id);
        EinvInvoiceAdjustmentsEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại"));

        if (Boolean.TRUE.equals(entity.getSubmittedToCqt())) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Biên bản đã được gửi cho Cơ quan Thuế, không thể xóa");
        }

        repository.delete(entity);
        log.info("[Adjustment] Đã xóa biên bản ID: {}", id);
    }
}