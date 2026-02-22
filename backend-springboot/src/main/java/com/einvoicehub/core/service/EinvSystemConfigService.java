package com.einvoicehub.core.service;

import com.einvoicehub.core.domain.entity.EinvSystemConfigEntity;
import com.einvoicehub.core.domain.repository.EinvSystemConfigRepository;
import com.einvoicehub.core.dto.EinvSystemConfigDto;
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
public class EinvSystemConfigService {

    private final EinvSystemConfigRepository repository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvSystemConfigDto> getAll() {
        log.info("[Config] Lấy danh sách tham số hệ thống");
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvSystemConfigDto getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Cấu hình không tồn tại"));
    }

    @Transactional
    public EinvSystemConfigDto create(EinvSystemConfigDto dto) {
        log.info("[Config] Thêm mới tham số: {}", dto.getConfigKey());

        if (repository.findByConfigKey(dto.getConfigKey()).isPresent()) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Khóa cấu hình đã tồn tại");
        }

        EinvSystemConfigEntity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public EinvSystemConfigDto update(Long id, EinvSystemConfigDto dto) {
        log.info("[Config] Thay đổi tham số hệ thống: {}", dto.getConfigKey());
        EinvSystemConfigEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Không tìm thấy tham số"));

        // Ràng buộc:Nếu cấu hình được đánh dấu là không được sửa (is_editable = 0)
        if (Boolean.FALSE.equals(entity.getIsEditable())) {
            log.error("[Config] Chặn thao tác sửa cấu hình hệ thống cố định: {}", entity.getConfigKey());
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Tham số này thuộc hệ thống lõi, không được phép chỉnh sửa");
        }

        entity.setConfigValue(dto.getConfigValue());
        entity.setDescription(dto.getDescription());

        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.warn("[Config] Yêu cầu xóa cấu hình ID: {}", id);
        EinvSystemConfigEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại"));
        if (Boolean.FALSE.equals(entity.getIsEditable())) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Không thể xóa cấu hình bắt buộc của hệ thống");
        }

        repository.delete(entity);
    }
}