package com.einvoicehub.core.service;

import com.einvoicehub.core.domain.entity.*;
import com.einvoicehub.core.domain.repository.*;
import com.einvoicehub.core.dto.EinvInvoiceTemplateRequest;
import com.einvoicehub.core.dto.EinvInvoiceTemplateResponse;
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
public class EinvInvoiceTemplateService {

    private final EinvInvoiceTemplateRepository repository;
    private final EinvMerchantRepository merchantRepository;
    private final EinvInvoiceTypeRepository typeRepository;
    private final EinvInvoiceRegistrationRepository registrationRepository;
    private final EinvInvoiceMetadataRepository metadataRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvInvoiceTemplateResponse> getAllByMerchant(Long merchantId) {
        log.info("[Template] Lấy danh sách mẫu hóa đơn cho Merchant ID: {}", merchantId);
        return repository.searchTemplates(merchantId, null).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvInvoiceTemplateResponse getById(Long id) {
        return repository.findWithDetailsById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Không tìm thấy cấu hình mẫu hóa đơn"));
    }

    @Transactional
    public EinvInvoiceTemplateResponse create(EinvInvoiceTemplateRequest request) {
        log.info("[Template] Đang cấu hình mẫu mới cho Merchant ID: {}, Ký hiệu: {}",
                request.getMerchantId(), request.getSymbolCode());

        EinvMerchantEntity merchant = merchantRepository.findById(request.getMerchantId())
                .filter(m -> !m.getIsDeleted())
                .orElseThrow(() -> new InvalidDataException(ErrorCode.MERCHANT_NOT_FOUND));

        EinvInvoiceTypeEntity type = typeRepository.findById(request.getInvoiceTypeId())
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Loại hóa đơn không tồn tại"));

        EinvInvoiceRegistrationEntity registration = null;
        if (request.getRegistrationId() != null) {
            registration = registrationRepository.findById(request.getRegistrationId())
                    .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Dải số đăng ký không tồn tại"));

            validateTemplateRangeAgainstRegistration(request.getMinNumber(), request.getMaxNumber(), registration);
        }

        EinvInvoiceTemplateEntity entity = mapper.toEntity(request);
        entity.setMerchant(merchant);
        entity.setInvoiceType(type);
        entity.setRegistration(registration);
        entity.setCurrentNumber(0);

        entity = repository.save(entity);
        log.info("[Template] Đã tạo thành công mẫu hóa đơn ID: {}", entity.getId());
        return mapper.toResponse(entity);
    }

    @Transactional
    public EinvInvoiceTemplateResponse update(Long id, EinvInvoiceTemplateRequest request) {
        log.info("[Template] Cập nhật mẫu hóa đơn ID: {}", id);

        EinvInvoiceTemplateEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Dữ liệu không tồn tại"));

        if (Boolean.TRUE.equals(entity.getMerchant().getIsDeleted())) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Merchant đã bị khóa");
        }

        entity.setTemplateCode(request.getTemplateCode());
        entity.setSymbolCode(request.getSymbolCode());
        entity.setIsActive(request.getIsActive());
        entity.setStartDate(request.getStartDate());
        entity.setProviderId(request.getProviderId());
        entity.setProviderSerialId(request.getProviderSerialId());

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.warn("[Template] Yêu cầu xóa mẫu hóa đơn ID: {}", id);

        if (!repository.existsById(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại");
        }

        if (metadataRepository.existsByTemplateId(id)) {
            log.error("[Template] Xóa thất bại: Mẫu ID {} đã phát sinh dữ liệu hóa đơn", id);
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Mẫu hóa đơn này đã được sử dụng để phát hành hóa đơn, không thể xóa");
        }

        repository.deleteById(id);
        log.info("[Template] Đã xóa thành công mẫu hóa đơn ID: {}", id);
    }

    private void validateTemplateRangeAgainstRegistration(Integer min, Integer max, EinvInvoiceRegistrationEntity reg) {
        if (min == null || max == null || min > max) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Giới hạn số hóa đơn không hợp lệ");
        }
        if (min < reg.getFromNumber() || max > reg.getToNumber()) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA,
                    String.format("Dải số của mẫu (%d-%d) phải nằm trong dải số đã đăng ký (%d-%d)",
                            min, max, reg.getFromNumber(), reg.getToNumber()));
        }
    }
}