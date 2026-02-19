package com.einvoicehub.core.service;

import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.domain.entity.EinvInvoiceStatusEntity;
import com.einvoicehub.core.domain.repository.EinvInvoiceStatusRepository;
import com.einvoicehub.core.domain.repository.EinvInvoiceMetadataRepository;
import com.einvoicehub.core.dto.EinvInvoiceStatusDto;
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
public class EinvInvoiceStatusService {

    private final EinvInvoiceStatusRepository repository;
    private final EinvInvoiceMetadataRepository metadataRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvInvoiceStatusDto> getAll() {
        log.info("[Catalog] Lấy danh mục trạng thái hóa đơn");
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvInvoiceStatusDto getById(Integer id) {
        log.info("[Catalog] Tìm trạng thái ID: {}", id);
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Trạng thái không tồn tại"));
    }

    @Transactional
    public EinvInvoiceStatusDto create(EinvInvoiceStatusDto dto) {
        log.info("[Catalog] Thêm mới trạng thái: {}", dto.getName());
        if (repository.findByName(dto.getName()).isPresent()) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Tên trạng thái đã tồn tại");
        }
        EinvInvoiceStatusEntity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public EinvInvoiceStatusDto update(Integer id, EinvInvoiceStatusDto dto) {
        log.info("[Catalog] Cập nhật trạng thái ID: {}", id);
        EinvInvoiceStatusEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Không tìm thấy trạng thái"));
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNote(dto.getNote());
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void delete(Integer id) {
        log.warn("[Catalog] Xóa trạng thái ID: {}", id);
        if (metadataRepository.existsByStatusId(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Dữ liệu đang được sử dụng trong Metadata hóa đơn");
        }
        repository.deleteById(id);
    }
}