package vn.softz.app.einvoicehub.service;

import vn.softz.app.einvoicehub.dto.EinvStoreProviderDto;
import vn.softz.app.einvoicehub.dto.EinvValidationResult;
import vn.softz.app.einvoicehub.dto.request.EinvStoreProviderRequest;

import java.util.List;
import java.util.Optional;

public interface EinvStoreProviderService {

    Optional<EinvStoreProviderDto> getConfig();

    Optional<EinvStoreProviderDto> getConfigByStoreId(String storeId);

    List<EinvStoreProviderDto> getConfigsByTenant(String tenantId);

    EinvStoreProviderDto saveConfig(EinvStoreProviderRequest request);

    EinvValidationResult validateConfig(EinvStoreProviderRequest request);

    EinvValidationResult deactivate();

    boolean isIntegrated(String storeId);
}
