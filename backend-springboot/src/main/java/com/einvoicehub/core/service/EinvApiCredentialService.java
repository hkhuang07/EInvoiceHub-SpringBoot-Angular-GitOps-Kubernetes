package com.einvoicehub.core.service;

import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.domain.entity.EinvApiCredentialsEntity;
import com.einvoicehub.core.domain.entity.EinvMerchantEntity;
import com.einvoicehub.core.domain.repository.EinvApiCredentialRepository;
import com.einvoicehub.core.domain.repository.EinvMerchantRepository;
import com.einvoicehub.core.dto.EinvApiCredentialDto;
import com.einvoicehub.core.mapper.EinvHubMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvApiCredentialService {

    private final EinvApiCredentialRepository repository;
    private final EinvMerchantRepository merchantRepository;
    private final EinvHubMapper mapper;

    @Transactional(readOnly = true)
    public List<EinvApiCredentialDto> getByMerchant(Long merchantId) {
        log.info("[API] Truy vấn danh sách API Key cho Merchant ID: {}", merchantId);
        // Lưu ý: Repository findByFilters trả về Page, ở đây lọc đơn giản
        return repository.findAll().stream()
                .filter(c -> c.getMerchant().getId().equals(merchantId))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EinvApiCredentialDto create(Long merchantId, String scopeJson) {
        log.info("[API] Tạo mới API Key cho Merchant ID: {}", merchantId);

        EinvMerchantEntity merchant = merchantRepository.findById(merchantId)
                .filter(m -> !m.getIsDeleted())
                .orElseThrow(() -> new InvalidDataException(ErrorCode.MERCHANT_NOT_FOUND));

        // Sinh ClientID và API Key thô (Logic SOFTZ cho tích hợp)
        String clientId = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        String rawApiKey = UUID.randomUUID().toString().replace("-", "");

        EinvApiCredentialsEntity entity = EinvApiCredentialsEntity.builder()
                .merchant(merchant)
                .clientId(clientId)
                .apiKeyPrefix(rawApiKey.substring(0, 8))
                .apiKeyHash("SHA256_HASH_OF_" + rawApiKey) // Thực tế dùng hashing chuẩn
                .scopes(scopeJson != null ? scopeJson : "[\"invoice:read\", \"invoice:create\"]")
                .isActive(true)
                .rateLimitPerHour(1000)
                .rateLimitPerDay(10000)
                .build();

        log.info("[API] Đã cấp mới API Key với ClientID: {}", clientId);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.warn("[API] Thu hồi API Key ID: {}", id);
        if (!repository.existsById(id)) {
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Khóa API không tồn tại");
        }
        repository.deleteById(id);
    }
}