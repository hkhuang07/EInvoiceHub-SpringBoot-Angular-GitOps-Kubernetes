package com.einvoicehub.core.service;

import com.einvoicehub.core.domain.entity.EinvInvoiceEntity;
import com.einvoicehub.core.domain.entity.EinvInvoiceSyncQueueEntity;
import com.einvoicehub.core.domain.entity.enums.SyncStatus;
import com.einvoicehub.core.domain.repository.EinvInvoiceMetadataRepository;
import com.einvoicehub.core.domain.repository.EinvInvoiceSyncQueueRepository;
import com.einvoicehub.core.dto.response.EinvInvoiceSyncQueueResponse;
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
public class EinvInvoiceSyncQueueService {

    private final EinvInvoiceSyncQueueRepository repository;
    private final EinvInvoiceMetadataRepository metadataRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvInvoiceSyncQueueResponse> getAll() {
        log.info("[Queue] Lấy danh sách hàng đợi đồng bộ");
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvInvoiceSyncQueueResponse getById(Long id) {
        log.info("[Queue] Truy vấn yêu cầu đồng bộ ID: {}", id);
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Yêu cầu đồng bộ không tồn tại"));
    }

    @Transactional
    public EinvInvoiceSyncQueueResponse create(EinvInvoiceSyncQueueResponse dto) {
        log.info("[Queue] Thêm mới yêu cầu đồng bộ cho hóa đơn ID: {}", dto.getInvoiceId());

        EinvInvoiceEntity invoice = metadataRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Hóa đơn gốc không tồn tại"));

        //Kiểm tra doanh nghiệp phải còn hoạt động mới được đồng bộ
        if (Boolean.TRUE.equals(invoice.getMerchant().getIsDeleted())) {
            throw new InvalidDataException(ErrorCode.MERCHANT_NOT_FOUND, "Doanh nghiệp đã ngừng hoạt động");
        }

        EinvInvoiceSyncQueueEntity entity = mapper.toEntity(dto);
        entity.setInvoice(invoice);
        entity.setAttemptCount(0); // Khởi tạo số lần thử lại là 0

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public EinvInvoiceSyncQueueResponse update(Long id, EinvInvoiceSyncQueueResponse dto) {
        log.info("[Queue] Cập nhật trạng thái xử lý yêu cầu ID: {}", id);
        EinvInvoiceSyncQueueEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại"));

        entity.setStatus(SyncStatus.valueOf(dto.getStatus()));
        entity.setErrorMessage(dto.getErrorMessage());
        entity.setAttemptCount(dto.getAttemptCount());
        entity.setProcessedBy(dto.getProcessedBy());

        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.warn("[Queue] Yêu cầu xóa khỏi hàng đợi ID: {}", id);
        EinvInvoiceSyncQueueEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại"));

        // Ràng buộc:không xóa yêu cầu đang được worker xử lý
        if (SyncStatus.PROCESSING.equals(entity.getStatus())) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Yêu cầu đang trong tiến trình xử lý, không thể xóa");
        }

        repository.delete(entity);
    }
}