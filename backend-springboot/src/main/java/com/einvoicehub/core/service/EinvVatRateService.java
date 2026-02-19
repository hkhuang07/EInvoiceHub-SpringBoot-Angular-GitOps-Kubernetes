package com.einvoicehub.core.service;

import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.domain.entity.EinvVatRateEntity;
import com.einvoicehub.core.domain.repository.EinvVatRateRepository;
import com.einvoicehub.core.domain.repository.EinvInvoiceItemRepository;
import com.einvoicehub.core.domain.repository.EinvInvoiceTaxBreakdownRepository;
import com.einvoicehub.core.dto.EinvVatRateDto;
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
public class EinvVatRateService {

    private final EinvVatRateRepository repository;
    private final EinvInvoiceItemRepository itemRepository;
    private final EinvInvoiceTaxBreakdownRepository taxRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvVatRateDto> getAll() {
        log.info("[Catalog] Lấy danh mục thuế suất GTGT");
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvVatRateDto getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Thuế suất không tồn tại"));
    }

    @Transactional
    public EinvVatRateDto create(EinvVatRateDto dto) {
        log.info("[Catalog] Tạo mới thuế suất: {}", dto.getRateCode());
        if (repository.findByRateCode(dto.getRateCode()).isPresent()) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Mã thuế suất đã tồn tại");
        }
        if (dto.getRatePercent() != null && dto.getRatePercent().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Tỷ lệ thuế suất không được âm");
        }
        EinvVatRateEntity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public EinvVatRateDto update(Long id, EinvVatRateDto dto) {
        EinvVatRateEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Không tìm thấy dữ liệu"));
        entity.setRateName(dto.getRateName());
        entity.setRatePercent(dto.getRatePercent());
        entity.setIsActive(dto.getIsActive());
        entity.setDisplayOrder(dto.getDisplayOrder());
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.warn("[Catalog] Xóa thuế suất ID: {}", id);
        if (itemRepository.existsByVatRateId(id) || taxRepository.existsByVatRateId(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Thuế suất đã được sử dụng trong chi tiết hóa đơn");
        }
        repository.deleteById(id);
    }
}