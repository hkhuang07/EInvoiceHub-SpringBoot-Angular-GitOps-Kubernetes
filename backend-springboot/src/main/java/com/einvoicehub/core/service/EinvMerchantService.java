package com.einvoicehub.core.service;

import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.domain.entity.EinvMerchantEntity;
import com.einvoicehub.core.domain.repository.EinvMerchantRepository;
import com.einvoicehub.core.domain.repository.EinvMerchantUserRepository;
import com.einvoicehub.core.domain.repository.EinvInvoiceMetadataRepository;
import com.einvoicehub.core.dto.EinvMerchantRequest;
import com.einvoicehub.core.dto.EinvMerchantResponse;
import com.einvoicehub.core.mapper.EinvHubMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvMerchantService {

    private final EinvMerchantRepository repository;
    private final EinvMerchantUserRepository userRepository;
    private final EinvInvoiceMetadataRepository metadataRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvMerchantResponse> getAll() {
        log.info("[Merchant] Lấy danh sách doanh nghiệp đang hoạt động");
        return repository.findAll().stream()
                .filter(m -> !m.getIsDeleted())
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvMerchantResponse getById(Long id) {
        log.info("[Merchant] Truy vấn thông tin doanh nghiệp ID: {}", id);
        return repository.findById(id)
                .filter(m -> !m.getIsDeleted())
                .map(mapper::toResponse)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.MERCHANT_NOT_FOUND));
    }

    @Transactional
    public EinvMerchantResponse create(EinvMerchantRequest request) {
        log.info("[Merchant] Đăng ký doanh nghiệp mới với MST: {}", request.getTaxCode());

        // 1. Kiểm tra MST đã tồn tại và chưa bị xóa
        if (repository.existsByTaxCodeAndIsDeletedFalse(request.getTaxCode())) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Mã số thuế này đã được đăng ký trên hệ thống");
        }

        EinvMerchantEntity entity = mapper.toEntity(request);
        // Thiết lập các giá trị mặc định theo SOFTZ
        entity.setIsDeleted(false);
        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Transactional
    public EinvMerchantResponse update(Long id, EinvMerchantRequest request) {
        log.info("[Merchant] Cập nhật thông tin doanh nghiệp ID: {}", id);
        EinvMerchantEntity entity = repository.findById(id)
                .filter(m -> !m.getIsDeleted())
                .orElseThrow(() -> new InvalidDataException(ErrorCode.MERCHANT_NOT_FOUND));

        // 2. Cập nhật các trường thông tin cơ bản
        entity.setCompanyName(request.getCompanyName());
        entity.setShortName(request.getShortName());
        entity.setAddress(request.getAddress());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setRepresentativeName(request.getRepresentativeName());
        entity.setTaxAuthorityCode(request.getTaxAuthorityCode());

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.warn("[Merchant] Yêu cầu xóa doanh nghiệp ID: {}", id);
        EinvMerchantEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.MERCHANT_NOT_FOUND));
        // 3. Kiểm tra ràng buộc giao dịch trước khi xóa
        if (metadataRepository.existsByMerchantId(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Không thể xóa doanh nghiệp đã phát sinh hóa đơn");
        }
        // 4. Soft Delete
        entity.setIsDeleted(true);
        entity.setDeletedAt(LocalDateTime.now());
        repository.save(entity);
        log.info("[Merchant] Đã thực hiện xóa mềm doanh nghiệp ID: {}", id);
    }
}