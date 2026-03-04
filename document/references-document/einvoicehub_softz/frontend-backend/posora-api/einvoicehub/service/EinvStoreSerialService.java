package vn.softz.app.einvoicehub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreSerialEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvStoreProviderEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreSerialRepository;
import vn.softz.app.einvoicehub.domain.repository.EinvStoreProviderRepository;
import vn.softz.app.einvoicehub.dto.EinvStoreSerialDto;
import vn.softz.app.einvoicehub.dto.EinvStoreSerialRequest;
import vn.softz.app.einvoicehub.mapper.EinvStoreSerialMapper;
import vn.softz.app.einvoicehub.provider.mobifone.MobifoneHttpClient;
import vn.softz.core.audit.TenantAware;
import vn.softz.core.common.Common;
import vn.softz.core.exception.InvalidDataException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EinvStoreSerialService {

    private final EinvStoreSerialRepository repository;
    private final EinvStoreSerialMapper mapper;
    private final MobifoneHttpClient mobifoneHttpClient;
    private final EinvStoreProviderRepository storeProviderRepository;

    private static final String MOBIFONE_PROVIDER = "MOBI";
    private static final String BKAV_PROVIDER = "BKAV";

    @TenantAware
    @Transactional(readOnly = true)
    public List<EinvStoreSerialDto> getList() {
        String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
        List<EinvStoreSerialEntity> entities = repository.findByStoreId(storeId);
        return mapper.toDtoList(entities);
    }

    @TenantAware
    @Transactional(readOnly = true)
    public List<EinvStoreSerialDto> getListByProvider(String providerId) {
        String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
        List<EinvStoreSerialEntity> entities = repository.findByStoreIdAndProviderId(storeId, providerId);
        return mapper.toDtoList(entities);
    }

    @TenantAware
    @Transactional(readOnly = true)
    public Optional<EinvStoreSerialDto> getById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @TenantAware
    @Transactional
    public EinvStoreSerialDto save(EinvStoreSerialRequest request) {
        String storeId = Common.getCurrentUser().map(u -> u.getLocId()).orElse(null);
        
        EinvStoreProviderEntity provider = storeProviderRepository.findByStoreId(storeId)
                .orElseThrow(() -> new InvalidDataException("einv.error.provider_not_configured"));
        
        if (provider.getStatus() == null || provider.getStatus() != 1) {
            throw new InvalidDataException("einv.error.provider_not_integrated");
        }
        
        EinvStoreSerialEntity entity;
        if (request.getId() != null && !request.getId().isEmpty()) {
            entity = repository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Serial not found: " + request.getId()));
            
            if (entity.getStatus() != null && entity.getStatus() == 1) {
                throw new InvalidDataException("einv.error.cannot_edit_approved");
            }
            
            mapper.updateEntity(request, entity);
        } else {
            entity = mapper.toEntity(request);
            entity.setStoreId(storeId);
        }
        
        if (entity.getStartDate() != null) {
            entity.setStartDate(entity.getStartDate().truncatedTo(java.time.temporal.ChronoUnit.DAYS));
        }
        
        repository.saveAndFlush(entity);
        log.info("Saved invoice serial: {} - {}", entity.getInvoiceForm(), entity.getInvoiceSerial());
        return mapper.toDto(entity);
    }

    @TenantAware
    @Transactional
    public void delete(String id) {
        repository.findById(id).ifPresent(entity -> {
            repository.delete(entity);
            log.info("Deleted invoice serial: {}", id);
        });
    }

    @TenantAware
    @Transactional
    @SuppressWarnings("unchecked")
    public EinvStoreSerialDto approve(String serialId) {
        EinvStoreSerialEntity entity = repository.findById(serialId)
                .orElseThrow(() -> new InvalidDataException("einv.error.serial_not_found"));

        String providerId = entity.getProviderId();
        String userSerial = entity.getInvoiceSerial();

        if (BKAV_PROVIDER.equalsIgnoreCase(providerId)) {
            entity.setStatus(1);
            repository.saveAndFlush(entity);
        } else if (MOBIFONE_PROVIDER.equalsIgnoreCase(providerId)) {
            approveMobifone(entity, userSerial);
        } else {
            throw new InvalidDataException("einv.error.provider_not_supported");
        }

        return mapper.toDto(entity);
    }

    @SuppressWarnings("unchecked")
    private void approveMobifone(EinvStoreSerialEntity entity, String userSerial) {
        Object response = mobifoneHttpClient.getInvoiceSeries();
        
        if (!(response instanceof List)) {
            throw new InvalidDataException("einv.error.mobifone_serial_list");
        }

        List<Map<String, Object>> seriesList = (List<Map<String, Object>>) response;
        
        String invoiceForm = entity.getInvoiceForm() != null ? entity.getInvoiceForm() : "";
        String combinedSerial = invoiceForm + userSerial;
        
        log.info("Validating MobiFone serial - Form: {}, Serial: {}, Combined: {}", 
                invoiceForm, userSerial, combinedSerial);
        
        Optional<Map<String, Object>> matchedSeries = seriesList.stream()
                .filter(item -> {
                    Object khhdon = item.get("khhdon");
                    if (khhdon != null) {
                        log.debug("Comparing {} with khhdon: {}", combinedSerial, khhdon.toString());
                    }
                    return khhdon != null && combinedSerial.equalsIgnoreCase(khhdon.toString());
                })
                .findFirst();

        if (matchedSeries.isEmpty()) {
            log.warn("Invoice serial '{}' (form: {}, serial: {}) not found in MobiFone list", 
                    combinedSerial, invoiceForm, userSerial);
            throw new InvalidDataException("einv.error.serial_not_registered");
        }

        Map<String, Object> matched = matchedSeries.get();
        String qlkhsdungId = matched.get("qlkhsdung_id") != null ? matched.get("qlkhsdung_id").toString() : null;

        entity.setProviderSerialId(qlkhsdungId);
        entity.setStatus(1);
        repository.saveAndFlush(entity);

        log.info("Approved MobiFone invoice serial: {} (form: {} + serial: {}) with qlkhsdung_id: {}", 
                combinedSerial, invoiceForm, userSerial, qlkhsdungId);
    }

    @TenantAware
    @Transactional
    public EinvStoreSerialDto deactivate(String serialId) {
        EinvStoreSerialEntity entity = repository.findById(serialId)
                .orElseThrow(() -> new InvalidDataException("einv.error.serial_not_found"));

        if (entity.getStatus() == null || entity.getStatus() != 1) {
            throw new InvalidDataException("einv.error.only_approved_can_deactivate");
        }

        entity.setStatus(8);
        repository.saveAndFlush(entity);
        log.info("Deactivated invoice serial: {} - {}", entity.getInvoiceForm(), entity.getInvoiceSerial());
        
        return mapper.toDto(entity);
    }
}
