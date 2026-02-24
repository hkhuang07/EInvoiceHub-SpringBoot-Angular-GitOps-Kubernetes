package com.einvoicehub.core.service;

import com.einvoicehub.core.domain.entity.EinvInvoiceEntity;
import com.einvoicehub.core.domain.entity.EinvTaxAuthorityResponseEntity;
import com.einvoicehub.core.domain.repository.EinvInvoiceMetadataRepository;
import com.einvoicehub.core.domain.repository.EinvTaxAuthorityResponseRepository;
import com.einvoicehub.core.dto.EinvTaxAuthorityResponseDto;
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
public class EinvTaxAuthorityResponseService {

    private final EinvTaxAuthorityResponseRepository repository;
    private final EinvInvoiceMetadataRepository metadataRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvTaxAuthorityResponseDto> getAll() {
        log.info("[CQT] Lấy danh sách toàn bộ phản hồi từ Thuế");
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvTaxAuthorityResponseDto getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Không tìm thấy phản hồi pháp lý"));
    }

    @Transactional
    public EinvTaxAuthorityResponseDto create(EinvTaxAuthorityResponseDto dto) {
        log.info("[CQT] Tiếp nhận mã CQT cho hóa đơn ID: {}", dto.getInvoiceId());

        EinvInvoiceEntity invoice = metadataRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Hóa đơn đích không tồn tại"));

        EinvTaxAuthorityResponseEntity entity = mapper.toEntity(dto);
        entity.setInvoice(invoice);

        entity = repository.save(entity);

        // Cập nhật ngược lại mã CQT (tax_authority_code) vào metadata hóa đơn
        if (entity.getCqtCode() != null) {
            invoice.setTaxAuthorityCode(entity.getCqtCode());
            metadataRepository.save(invoice);
            log.info("[CQT] Đã đồng bộ mã CQT {} vào Metadata hóa đơn", entity.getCqtCode());
        }

        return mapper.toDto(entity);
    }

    @Transactional
    public EinvTaxAuthorityResponseDto update(Long id, EinvTaxAuthorityResponseDto dto) {
        log.info("[CQT] Cập nhật diễn giải phản hồi ID: {}", id);
        EinvTaxAuthorityResponseEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Dữ liệu không tồn tại"));

        entity.setErrorMessage(dto.getErrorMessage());
        entity.setStatusFromCqt(dto.getStatusFromCqt());

        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.error("[CQT] CẢNH BÁO: Thao tác xóa phản hồi Thuế nhạy cảm ID: {}", id);
        if (!repository.existsById(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại");
        }
        //logs này chỉ Admin mới được xóa
        repository.deleteById(id);
    }
}