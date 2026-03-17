package vn.softz.app.einvoicehub.service.catalog;

import vn.softz.app.einvoicehub.domain.entity.EinvProviderEntity;

import java.util.List;
import java.util.Optional;

public interface EinvProviderService extends CatalogService<EinvProviderEntity, String> {

   
    List<EinvProviderEntity> findAllActive();


    Optional<EinvProviderEntity> findByCode(String providerCode);


    EinvProviderEntity activate(String id);

    /**@param id ID của NCC cần deactivate
     * @return entity sau khi cập nhật
     * @throws vn.softz.app.einvoicehub.exception.BusinessException nếu còn Store đang tích hợp*/
    EinvProviderEntity deactivate(String id);
}
