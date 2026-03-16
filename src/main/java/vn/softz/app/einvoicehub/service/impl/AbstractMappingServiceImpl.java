package vn.softz.app.einvoicehub.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.core.exception.BusinessException;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractMappingServiceImpl<D, HID> {

    protected abstract String mappingName();
    protected abstract List<D>      doFindByProvider(String providerId);
    protected abstract List<D>      doFindActive(String providerId);
    protected abstract Optional<D>  doFindById(String id);
    protected abstract Optional<String> doLookupProviderCode(String providerId, HID hubId);
    protected abstract Optional<HID>    doLookupHubId(String providerId, String providerCode);
    protected abstract D  doCreate(D dto);
    protected abstract D  doUpdate(String id, D dto);
    protected abstract void doSoftDelete(String id);

    @Transactional(readOnly = true)
    public List<D> findAllByProvider(String providerId) {
        return doFindByProvider(providerId);
    }

    @Transactional(readOnly = true)
    public List<D> findActiveByProvider(String providerId) {
        return doFindActive(providerId);
    }

    @Transactional(readOnly = true)
    public Optional<D> findById(String id) {
        return doFindById(id);
    }

    @Transactional
    public D create(D dto) {
        return doCreate(dto);
    }

    @Transactional
    public D update(String id, D dto) {
        return doUpdate(id, dto);
    }

    @Transactional
    public void delete(String id) {
        doSoftDelete(id);
    }

    @Transactional(readOnly = true)
    public Optional<String> findProviderCode(String providerId, HID hubId) {
        return doLookupProviderCode(providerId, hubId);
    }

    @Transactional(readOnly = true)
    public Optional<HID> findHubId(String providerId, String providerCode) {
        return doLookupHubId(providerId, providerCode);
    }

    @Transactional(readOnly = true)
    public String findProviderCodeOrDefault(String providerId, HID hubId, String fallback) {
        Optional<String> result = doLookupProviderCode(providerId, hubId);
        if (result.isEmpty()) {
            log.warn("[mapping-{}] Hub→Provider not found: providerId={}, hubId={} → fallback={}",
                     mappingName(), providerId, hubId, fallback);
        }
        return result.orElse(fallback);
    }

    @Transactional(readOnly = true)
    public HID findHubIdOrDefault(String providerId, String providerCode, HID fallback) {
        Optional<HID> result = doLookupHubId(providerId, providerCode);
        if (result.isEmpty()) {
            log.warn("[mapping-{}] Provider→Hub not found: providerId={}, providerCode={} → fallback={}",
                     mappingName(), providerId, providerCode, fallback);
        }
        return result.orElse(fallback);
    }

    // ── Shared helper ─────────────────────────────────────────────────────────

    protected BusinessException notFound(String id) {
        return new BusinessException(
            String.format("einv.error.mapping_%s_not_found: id=%s",
                          mappingName().toLowerCase(), id));
    }

    protected BusinessException duplicateMapping(String providerId, Object hubId) {
        return new BusinessException(
            String.format("einv.error.mapping_%s_duplicate: providerId=%s, hubId=%s",
                          mappingName().toLowerCase(), providerId, hubId));
    }
}
