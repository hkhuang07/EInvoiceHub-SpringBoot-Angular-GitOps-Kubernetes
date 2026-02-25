package com.einvoicehub.core.service;

import com.einvoicehub.core.domain.entity.MerchantEntity;
import com.einvoicehub.core.domain.entity.EinvStoreProviderEntity;
import com.einvoicehub.core.domain.entity.EinvProviderEntity;
import com.einvoicehub.core.domain.repository.EinvInvoiceMetadataRepository;
import com.einvoicehub.core.domain.repository.EinvMerchantProviderConfigRepository;
import com.einvoicehub.core.domain.repository.EinvMerchantRepository;
import com.einvoicehub.core.domain.repository.EinvProviderRepository;
import com.einvoicehub.core.dto.EinvMerchantProviderConfigRequest;
import com.einvoicehub.core.dto.EinvMerchantProviderConfigResponse;
import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.mapper.EinvHubMapper;
import com.einvoicehub.core.provider.EInvoiceProviderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvMerchantProviderConfigService {

    private final EinvMerchantProviderConfigRepository repository;
    private final EinvMerchantRepository merchantRepository;
    private final EinvProviderRepository providerRepository;
    private final EinvInvoiceMetadataRepository metadataRepository;
    private final EInvoiceProviderFactory providerFactory;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvMerchantProviderConfigResponse> getAll() {
        log.info("[Config] Đang truy vấn danh sách toàn bộ cấu hình kết nối hệ thống");
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EinvMerchantProviderConfigResponse> getByMerchantId(Long merchantId) {
        log.info("[Config] Đang truy vấn danh sách cấu hình kết nối cho Merchant ID: {}", merchantId);
        return repository.searchConfigs(merchantId, null).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EinvMerchantProviderConfigResponse getById(Long id) {
        log.info("[Config] Truy vấn chi tiết cấu hình ID: {}", id);
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> {
                    log.error("[Config] Không tìm thấy cấu hình với ID: {}", id);
                    return new InvalidDataException(ErrorCode.INVALID_DATA, "Không tìm thấy cấu hình kết nối");
                });
    }


    /*public EinvInvoiceProvider getActiveProviderForMerchant(Long merchantId) {
        EinvMerchantProviderConfigEntity config = repository.findByMerchantIdAndIsDefaultTrueAndIsActiveTrue(merchantId)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA,
                        "Doanh nghiệp chưa thiết lập cấu hình nhà cung cấp hóa đơn mặc định"));

        String providerCode = config.getProvider().getProviderCode();

        log.debug("[Config] Merchant {} đang sử dụng nhà cung cấp: {}", merchantId, providerCode);

        return providerFactory.getProvider(providerCode);
    }*/

    @Transactional
    public EinvMerchantProviderConfigResponse create(EinvMerchantProviderConfigRequest request) {
        log.info("[Config] Đang tạo cấu hình mới cho Merchant {} với Provider {}",
                request.getMerchantId(), request.getProviderId());

        MerchantEntity merchant = merchantRepository.findById(request.getMerchantId())
                .filter(m -> !m.getIsDeleted())
                .orElseThrow(() -> new InvalidDataException(ErrorCode.MERCHANT_NOT_FOUND));

        EinvProviderEntity provider = providerRepository.findById(request.getProviderId())
                .filter(EinvProviderEntity::getIsActive)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Nhà cung cấp không tồn tại hoặc ngừng hỗ trợ"));

        if (repository.existsByMerchantIdAndProviderId(request.getMerchantId(), request.getProviderId())) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Doanh nghiệp đã cấu hình tài khoản cho nhà cung cấp này");
        }

        EinvStoreProviderEntity entity = mapper.toEntity(request);
        entity.setMerchant(merchant);
        entity.setProvider(provider);

        entity.setPasswordServiceEncrypted("ENC_" + request.getPasswordService());

        handleDefaultConfigLogic(merchant.getId(), request.getIsDefault(), entity);

        entity = repository.save(entity);
        log.info("[Config] Đã tạo thành công cấu hình kết nối ID: {}", entity.getId());
        return mapper.toResponse(entity);
    }

    @Transactional
    public EinvMerchantProviderConfigResponse update(Long id, EinvMerchantProviderConfigRequest request) {
        log.info("[Config] Đang cập nhật cấu hình ID: {}", id);

        EinvStoreProviderEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Dữ liệu không tồn tại để cập nhật"));

        if (Boolean.TRUE.equals(entity.getMerchant().getIsDeleted())) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Doanh nghiệp đã ngừng hoạt động, không được sửa cấu hình");
        }

        entity.setPartnerId(request.getPartnerId());
        entity.setPartnerToken(request.getPartnerToken());
        entity.setTaxCode(request.getTaxCode());
        entity.setIntegrationUrl(request.getIntegrationUrl());
        entity.setUsernameService(request.getUsernameService());
        entity.setIsTestMode(request.getIsTestMode());
        entity.setIsActive(request.getIsActive());
        entity.setExtraConfig(request.getExtraConfig());

        if (request.getPasswordService() != null && !request.getPasswordService().isBlank()) {
            entity.setPasswordServiceEncrypted("ENC_UPDATED_" + request.getPasswordService());
        }

        handleDefaultConfigLogic(entity.getMerchant().getId(), request.getIsDefault(), entity);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.warn("[Config] Đang thực hiện yêu cầu xóa cấu hình ID: {}", id);

        if (!repository.existsById(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Bản ghi không tồn tại");
        }

        if (metadataRepository.existsByProviderConfigId(id)) {
            log.error("[Config] Xóa thất bại: Cấu hình ID {} đã phát sinh dữ liệu hóa đơn", id);
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Không thể xóa cấu hình đã được sử dụng để phát hành hóa đơn");
        }

        repository.deleteById(id);
        log.info("[Config] Đã xóa thành công cấu hình ID: {}", id);
    }

    private void handleDefaultConfigLogic(Long merchantId, Boolean isNewDefault, EinvStoreProviderEntity currentEntity) {
        if (Boolean.TRUE.equals(isNewDefault)) {
            // Tìm cấu hình mặc định cũ và gỡ bỏ
            repository.findByMerchantIdAndIsDefaultTrueAndIsActiveTrue(merchantId).ifPresent(oldDefault -> {
                if (!oldDefault.getId().equals(currentEntity.getId())) {
                    oldDefault.setIsDefault(false);
                    repository.save(oldDefault);
                    log.debug("[Config] Đã tắt trạng thái mặc định của cấu hình cũ ID: {}", oldDefault.getId());
                }
            });
            currentEntity.setIsDefault(true);
        } else {
            currentEntity.setIsDefault(false);
        }
    }
}