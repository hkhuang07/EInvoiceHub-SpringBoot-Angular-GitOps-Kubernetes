package vn.softz.app.einvoicehub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderHistoryEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderHistoryRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreSerialRepository;
import vn.softz.app.einvoicehub.dto.EinvStoreProviderDto;
import vn.softz.app.einvoicehub.dto.EinvValidationResult;
import vn.softz.app.einvoicehub.dto.request.EinvStoreProviderRequest;
import vn.softz.app.einvoicehub.mapper.EinvStoreProviderMapper;
import vn.softz.app.einvoicehub.provider.bkav.BkavSoapClient;
import vn.softz.app.einvoicehub.provider.bkav.constant.BkavCommandType;
import vn.softz.app.einvoicehub.provider.bkav.model.BkavResponse;
//import vn.softz.app.einvoicehub.provider.mobifone.MobifoneHttpClient;
//import vn.softz.app.einvoicehub.provider.mobifone.model.MobifoneLoginResponse;
import vn.softz.app.einvoicehub.service.EinvStoreProviderService;
import vn.softz.app.einvoicehub.audit.TenantAware;
import vn.softz.app.einvoicehub.common.Common;
import vn.softz.app.einvoicehub.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvStoreProviderServiceImpl implements EinvStoreProviderService {

    private final EinvStoreProviderRepository           repository;
    private final EinvStoreProviderHistoryRepository    historyRepository;
    private final EinvStoreSerialRepository             serialRepository;

    private final EinvStoreProviderMapper               mapper;

    private final BkavSoapClient                          bkavSoapClient;
    //private final MobifoneHttpClient                    mobifoneHttpClient;
    //private final BCryptPasswordEncoder                 passwordEncoder;

    private static final String PROVIDER_BKAV     = "BKAV";
    //private static final String PROVIDER_MOBIFONE = "MOBI";

    private static final byte STATUS_PENDING     = 0;
    private static final byte STATUS_ACTIVE      = 1;
    private static final byte STATUS_DEACTIVATED = 8;

    // Query Methods
    @Override
    @TenantAware
    @Transactional(readOnly = true)
    public Optional<EinvStoreProviderDto> getConfig() {
        String storeId = resolveCurrentStoreId();
        return repository.findByStoreId(storeId).map(this::toDtoMasked);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EinvStoreProviderDto> getConfigByStoreId(String storeId) {
        return repository.findByStoreId(storeId)
                         .map(this::toDtoMasked);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EinvStoreProviderDto> getConfigsByTenant(String tenantId) {
        return repository.findByTenantId(tenantId)
                         .stream()
                         .map(this::toDtoMasked)
                         .toList();
    }

    // Write Methods
    @Override
    @TenantAware
    @Transactional
    public EinvStoreProviderDto saveConfig(EinvStoreProviderRequest request) {
        String storeId  = resolveCurrentStoreId();
        String tenantId = resolveCurrentTenantId();

        boolean isNew = !repository.findByStoreId(storeId).isPresent();

        EinvStoreProviderEntity entity = repository.findByStoreId(storeId)
                .orElseGet(() -> {
                    EinvStoreProviderEntity e = new EinvStoreProviderEntity();
                    e.setStoreId(storeId);
                    e.setTenantId(tenantId);
                    return e;
                });

        mapper.updateEntity(request, entity);
        /*if (hasValue(request.getPartnerPwd())) {
            String encoded = passwordEncoder.encode(request.getPartnerPwd());
            entity.setPartnerPwd(encoded);
            log.info("[saveConfig] partner_pwd re-encoded for storeId={}", storeId);
        }
        if (hasValue(request.getPasswordService())) {
            String encoded = passwordEncoder.encode(request.getPasswordService());
            entity.setPasswordService(encoded);
            log.info("[saveConfig] password_service re-encoded for storeId={}", storeId);
        }

        if ("VNPT".equalsIgnoreCase(request.getProviderId())
                && hasValue(request.getIntegrationUrl())) {
            entity.setIntegrationUrl(request.getIntegrationUrl());
        }*/

        entity.setStatus(STATUS_PENDING);
        entity.setIntegratedDate(null);

        repository.saveAndFlush(entity);
        log.info("[saveConfig] {} config for storeId={}, providerId={}",
                isNew ? "Created" : "Updated", storeId, entity.getProviderId());

        saveHistory(entity, isNew ? "CREATE" : "UPDATE", entity.getStatus(), null);

        return toDtoMasked(entity);
    }

    @Override
    @TenantAware
    @Transactional
    public EinvValidationResult validateConfig(EinvStoreProviderRequest request) {
        String providerId = request.getProviderId();
        log.info("[validateConfig] Validating provider={}", providerId);

        try {
            EinvValidationResult result = switch (providerId.toUpperCase()) {
                case PROVIDER_BKAV     -> validateBkav();
                //case PROVIDER_MOBIFONE -> validateMobifone();
                default -> EinvValidationResult.error(providerId,"einv.error.provider_not_supported!");
            };

            if (result.isSuccess()) {
                String storeId = resolveCurrentStoreId();
                repository.findByStoreId(storeId).ifPresent(entity -> {
                    entity.setStatus(STATUS_ACTIVE);
                    entity.setIntegratedDate(LocalDateTime.now());
                    repository.save(entity);
                    saveHistory(entity, "VALIDATE_SUCCESS", STATUS_ACTIVE, null);
                    log.info("[validateConfig] Integration SUCCESS for storeId={}", storeId);
                });
            }

            return result;

        } catch (Exception e) {
            log.error("[validateConfig] Unexpected error for provider={}: {}", providerId, e.getMessage(), e);
            return EinvValidationResult.error("einv.error.connection: " , e.getMessage());
        }
    }

    @Override
    @TenantAware
    @Transactional
    public EinvValidationResult deactivate() {
        String storeId = resolveCurrentStoreId();

        if (serialRepository.existsByStoreIdAndStatus(storeId, STATUS_ACTIVE)) {
            log.warn("[deactivate] Cannot deactivate: storeId={} has active serials", storeId);
            return EinvValidationResult.error("storeId","einv.error.cannot_cancel_has_approved");
        }

        repository.findByStoreId(storeId).ifPresent(entity -> {
            entity.setStatus(STATUS_DEACTIVATED);
            entity.setIntegratedDate(null);
            repository.save(entity);
            saveHistory(entity, "DEACTIVATE", STATUS_DEACTIVATED, "Hủy tích hợp NCC");
            log.info("[deactivate] Deactivated config id={} for storeId={}", entity.getId(), storeId);
        });

        return EinvValidationResult.success("einv_config.msg.cancelled");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isIntegrated(String storeId) {
        return repository.findByStoreId(storeId)
                         .map(e -> STATUS_ACTIVE == (e.getStatus() != null ? e.getStatus() : -1))
                         .orElse(false);
    }

    private EinvValidationResult validateBkav() {
        String storeId = resolveCurrentStoreId();
        EinvStoreProviderEntity entity = repository.findByStoreId(storeId)
                .orElseThrow(() -> new BusinessException("einv.error.config_not_saved"));

        String taxCode = entity.getTaxCode();
        if (!hasValue(taxCode)) {
            return EinvValidationResult.error("taxCode","einv.error.missing_tax_code");
        }

        try {
            BkavResponse result = bkavSoapClient.executeCommand(
                    storeId,
                    BkavCommandType.GET_UNIT_INFO_BY_TAXCODE,
                    taxCode);

            if (result.isSuccess()) {
                log.info("[validateBkav] BKAV connection success for taxCode={}", taxCode);
                return EinvValidationResult.success("einv.success.bkav_connected");
            } else {
                log.warn("[validateBkav] BKAV returned error: {}", result.getErrorMessage());
                return EinvValidationResult.error("provider","einv.error.validation_errored");
            }
        } catch (Exception e) {
            log.error("[validateBkav] SOAP call errored: {}", e.getMessage());
            return EinvValidationResult.error("provider","einv.error.validation_errored");
        }
    }


    private EinvValidationResult validateMobifone() {
        String storeId = resolveCurrentStoreId();
        EinvStoreProviderEntity entity = repository.findByStoreId(storeId)
                .orElseThrow(() -> new BusinessException("einv.error.config_not_saved"));

        String username = entity.getPartnerUsr();
        String taxCode  = entity.getTaxCode();

        if (!hasValue(username)) {
            return EinvValidationResult.error("store","einv.error.missing_credentials");
        }

        // DESIGN NOTE: partner_pwd trong DB là BCrypt hash → không thể gửi thẳng.
        // Cần client gửi lại plaintext trong request HOẶC dùng AES vault.
        // Tạm thời: yêu cầu endpoint validate nhận lại password trong body.
        // TODO: Replace với AES-encrypted secret vault (phase 2).
        log.warn("[validateMobifone] password strategy: plaintext required from client for API call");
        return EinvValidationResult.error("provider","einv.error.mobifone_resubmit_password_required");
    }

    // Private: History Helper
    private void saveHistory(EinvStoreProviderEntity entity,
                             String actionType,
                             Byte status,
                             String notes) {
        try {
            EinvStoreProviderHistoryEntity history = new EinvStoreProviderHistoryEntity();
            history.setTenantId(entity.getTenantId());
            history.setStoreId(entity.getStoreId());
            history.setProviderId(entity.getProviderId());
            history.setActionType(actionType);
            history.setStatus(status);
            history.setNotes(notes);
            historyRepository.save(history);
        } catch (Exception ex) {
            // History errorure KHÔNG được rollback transaction chính
            log.warn("[saveHistory] errored to record history for storeId={}: {}",
                     entity.getStoreId(), ex.getMessage());
        }
    }

    // Private: DTO Masking – ẩn mật khẩu trước khi trả ra client
    private EinvStoreProviderDto toDtoMasked(EinvStoreProviderEntity entity) {
        EinvStoreProviderDto dto = mapper.toDto(entity);
        // Che các trường credential – client không cần thấy hash
        if (dto.getPartnerToken()    != null) dto.setPartnerToken("***");
        if (dto.getPartnerPwd()      != null) dto.setPartnerPwd("***");
        if (dto.getPasswordService() != null) dto.setPasswordService("***");
        return dto;
    }

    // Private: Context Helpers
    private String resolveCurrentStoreId() {
        return Common.getCurrentUser()
                     .map(u -> u.getLocId())
                     .orElseThrow(() -> new BusinessException("einv.error.no_store_context"));
    }

    private String resolveCurrentTenantId() {
        return Common.getCurrentUser()
                     .map(u -> u.getTenantId())
                     .orElseThrow(() -> new BusinessException("einv.error.no_tenant_context"));
    }

    private boolean hasValue(String s) {
        return s != null && !s.isBlank();
    }
}
