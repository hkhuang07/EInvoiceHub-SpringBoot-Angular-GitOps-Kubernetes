package com.einvoicehub.core.service;

import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.domain.entity.EinvInvoiceTypeEntity;
import com.einvoicehub.core.domain.repository.EinvInvoiceTypeRepository;
import com.einvoicehub.core.domain.repository.EinvInvoiceTemplateRepository;
import com.einvoicehub.core.domain.repository.EinvInvoiceMetadataRepository;
import com.einvoicehub.core.dto.EinvInvoiceTypeDto;
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
public class EinvInvoiceTypeService {

    private final EinvInvoiceTypeRepository repository;
    private final EinvInvoiceTemplateRepository templateRepository;
    private final EinvInvoiceMetadataRepository metadataRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvInvoiceTypeDto> getAll() {
        log.info("[Catalog] Lấy toàn bộ danh mục loại hóa đơn");
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvInvoiceTypeDto getById(Long id) {
        log.info("[Catalog] Lấy chi tiết loại hóa đơn ID: {}", id);
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Loại hóa đơn không tồn tại"));
    }

    @Transactional
    public EinvInvoiceTypeDto create(EinvInvoiceTypeDto dto) {
        log.info("[Catalog] Tạo mới loại hóa đơn: {}", dto.getTypeCode());
        if (repository.findByTypeCode(dto.getTypeCode()).isPresent()) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Mã loại hóa đơn " + dto.getTypeCode() + " đã tồn tại");
        }
        EinvInvoiceTypeEntity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public EinvInvoiceTypeDto update(Long id, EinvInvoiceTypeDto dto) {
        log.info("[Catalog] Cập nhật loại hóa đơn ID: {}", id);
        EinvInvoiceTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Dữ liệu không tồn tại"));

        entity.setTypeName(dto.getTypeName());
        entity.setDescription(dto.getDescription());
        entity.setIsActive(dto.getIsActive());
        entity.setDisplayOrder(dto.getDisplayOrder());
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.warn("[Catalog] Yêu cầu xóa loại hóa đơn ID: {}", id);
        EinvInvoiceTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại"));

        if (templateRepository.existsByInvoiceTypeId(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Không thể xóa vì đã có Mẫu hóa đơn tham chiếu");
        }
        if (metadataRepository.existsByInvoiceTypeCode(entity.getTypeCode())) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Không thể xóa vì đã có Hóa đơn phát sinh mã này");
        }

        repository.delete(entity);
        log.info("[Catalog] Đã xóa loại hóa đơn: {}", entity.getTypeCode());
    }
}