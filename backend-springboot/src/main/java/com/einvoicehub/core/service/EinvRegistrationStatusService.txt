package com.einvoicehub.core.service;

import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.domain.entity.EinvRegistrationStatusEntity;
import com.einvoicehub.core.domain.repository.EinvRegistrationStatusRepository;
import com.einvoicehub.core.domain.repository.EinvInvoiceRegistrationRepository;
import com.einvoicehub.core.dto.EinvRegistrationStatusDto;
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
public class EinvRegistrationStatusService {

    private final EinvRegistrationStatusRepository repository;
    private final EinvInvoiceRegistrationRepository registrationRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvRegistrationStatusDto> getAll() {
        log.info("[Catalog] Lấy danh mục trạng thái đăng ký");
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvRegistrationStatusDto getById(Integer id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Trạng thái không tồn tại"));
    }

    @Transactional
    public EinvRegistrationStatusDto create(EinvRegistrationStatusDto dto) {
        log.info("[Catalog] Tạo trạng thái đăng ký: {}", dto.getName());
        if (repository.findByName(dto.getName()).isPresent()) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Tên trạng thái đã tồn tại");
        }
        EinvRegistrationStatusEntity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public EinvRegistrationStatusDto update(Integer id, EinvRegistrationStatusDto dto) {
        EinvRegistrationStatusEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Không tìm thấy dữ liệu"));
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNote(dto.getNote());
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void delete(Integer id) {
        log.warn("[Catalog] Xóa trạng thái đăng ký ID: {}", id);
        if (registrationRepository.existsByStatusId(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Trạng thái này đang được sử dụng cho dải số hóa đơn");
        }
        repository.deleteById(id);
    }
}