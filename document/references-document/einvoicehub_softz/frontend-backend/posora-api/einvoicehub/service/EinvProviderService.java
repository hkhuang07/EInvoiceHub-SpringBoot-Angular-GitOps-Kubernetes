package vn.softz.app.einvoicehub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softz.app.einvoicehub.domain.entity.EinvProviderEntity;
import vn.softz.app.einvoicehub.domain.repository.EinvProviderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EinvProviderService {

    private final EinvProviderRepository repository;

    @Transactional(readOnly = true)
    public List<EinvProviderEntity> getActiveProviders() {
        return repository.findByInactiveFalse();
    }
}
