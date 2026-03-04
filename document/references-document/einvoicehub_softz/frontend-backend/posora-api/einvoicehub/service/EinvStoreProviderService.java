package vn.softz.app.einvoicehub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreSerialRepository;
import vn.softz.app.einvoicehub.dto.EinvStoreProviderDto;
import vn.softz.app.einvoicehub.dto.EinvStoreProviderRequest;
import vn.softz.app.einvoicehub.dto.EinvValidationResult;
import vn.softz.app.einvoicehub.mapper.EinvStoreProviderMapper;
import vn.softz.app.einvoicehub.provider.bkav.BkavSoapClient;
import vn.softz.app.einvoicehub.provider.bkav.model.BkavResponse;
import vn.softz.app.einvoicehub.provider.bkav.constant.BkavCommandType;
import vn.softz.app.einvoicehub.provider.mobifone.MobifoneHttpClient;
import vn.softz.app.einvoicehub.provider.mobifone.model.MobifoneLoginResponse;
import vn.softz.core.audit.TenantAware;
import vn.softz.core.common.Common;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvStoreProviderService {

    private final EinvStoreProviderRepository repository;
    private final EinvStoreSerialRepository serialRepository;
    private final EinvStoreProviderMapper mapper;
    private final BkavSoapClient bkavSoapClient;
    private final MobifoneHttpClient mobifoneHttpClient;
    
    public static final String BKAV = "BKAV";
    public static final String MOBIFONE = "MOBI";

    @TenantAware
    @Transactional(readOnly = true)
    public Optional<EinvStoreProviderDto> getConfig() {
        String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
        return repository.findByStoreId(storeId).map(mapper::toDto);
    }

    @TenantAware
    @Transactional
    public EinvStoreProviderDto saveConfig(EinvStoreProviderRequest request) {
        String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
        
        EinvStoreProviderEntity entity = repository.findByStoreId(storeId)
                .orElseGet(() -> {
                    EinvStoreProviderEntity newEntity = new EinvStoreProviderEntity();
                    newEntity.setStoreId(storeId);
                    return newEntity;
                });
        
        mapper.updateEntity(request, entity);
        entity.setStatus(0);
        if ("VNPT".equalsIgnoreCase(request.getProviderId())) {
            entity.setIntegrationUrl(request.getIntegrationUrl());
        }
        
        repository.saveAndFlush(entity);
        return mapper.toDto(entity);
    }

    @TenantAware
    @Transactional
    public EinvValidationResult deactivate() {
        String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
        
        if (serialRepository.existsByStoreIdAndStatus(storeId, 1)) {
            return EinvValidationResult.fail("einv.error.cannot_cancel_has_approved");
        }
        
        repository.findByStoreId(storeId).ifPresent(entity -> {
            entity.setStatus(8);
            entity.setIntegratedDate(null);
            repository.save(entity);
            log.info("Deactivated e-invoice config: {}", entity.getId());
        });

        return EinvValidationResult.ok("einv_config.msg.cancelled");
    }

    @TenantAware
    @Transactional
    public EinvValidationResult validateConfig(EinvStoreProviderRequest request) {
        String providerId = request.getProviderId();
        log.info("Validating e-invoice provider: {}", providerId);
        
        try {
            EinvValidationResult result;
            if (BKAV.equalsIgnoreCase(providerId)) {
                result = validateBkav(request);
            } else if (MOBIFONE.equalsIgnoreCase(providerId)) {
                result = validateMobifone(request);
            } else {
                return EinvValidationResult.fail("Provider chưa được hỗ trợ: " + providerId);
            }
            
            if (result.isSuccess()) {
                String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
                repository.findByStoreId(storeId).ifPresent(entity -> {
                    entity.setStatus(1);
                    entity.setIntegratedDate(Instant.now());
                    repository.save(entity);
                });
            }
            
            return result;
        } catch (Exception e) {
            log.error("Error validating provider {}: {}", providerId, e.getMessage());
            return EinvValidationResult.fail("Lỗi kết nối: " + e.getMessage());
        }
    }

    private EinvValidationResult validateBkav(EinvStoreProviderRequest request) {
        try {
            String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
            var entityOpt = repository.findByStoreId(storeId);
            if (entityOpt.isEmpty()) {
                return EinvValidationResult.fail("einv.error.config_not_saved");
            }
            String taxCode = entityOpt.get().getTaxCode();
            
            BkavResponse result = bkavSoapClient.executeCommand(
                    BkavCommandType.GET_UNIT_INFO_BY_TAXCODE, 
                    taxCode != null ? taxCode : "0317136465");
                    
            if (result.isSuccess()) {
                return EinvValidationResult.ok("einv.success.bkav_connected");
            } else {
                log.warn("BKAV validation failed: {}", result.getErrorMessage());
                return EinvValidationResult.fail("einv.error.validation_failed");
            }
        } catch (Exception e) {
            log.error("BKAV validation error: {}", e.getMessage());
            return EinvValidationResult.fail("einv.error.validation_failed");
        }
    }

    private EinvValidationResult validateMobifone(EinvStoreProviderRequest request) {
        try {
            String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
            var entityOpt = repository.findByStoreId(storeId);
            if (entityOpt.isEmpty()) {
                return EinvValidationResult.fail("einv.error.config_not_saved");
            }
            var entity = entityOpt.get();
            
            String username = entity.getPartnerUsr();
            String password = entity.getPartnerPwd();
            String taxCode = entity.getTaxCode();
            
            if (username == null || password == null) {
                return EinvValidationResult.fail("einv.error.missing_credentials");
            }
            
            MobifoneLoginResponse response = mobifoneHttpClient.loginWithCredentials(username, password, taxCode);
            if (response.getToken() != null) {
                return EinvValidationResult.ok("einv.success.mobifone_connected");
            } else {
                return EinvValidationResult.fail("einv.error.mobifone_validation_failed");
            }
        } catch (Exception e) {
            log.error("MobiFone validation error: {}", e.getMessage());
            return EinvValidationResult.fail("einv.error.mobifone_connection_error");
        }
    }
}
