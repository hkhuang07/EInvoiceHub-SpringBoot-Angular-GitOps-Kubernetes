package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvMappingItemTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvMappingItemTypeRepository extends JpaRepository<EinvMappingItemTypeEntity, String>, JpaSpecificationExecutor<EinvMappingItemTypeEntity> {

    List<EinvMappingItemTypeEntity> findByProviderId(String providerId);

    List<EinvMappingItemTypeEntity> findByItemTypeId(Byte itemTypeId);

    Optional<EinvMappingItemTypeEntity> findByProviderIdAndItemTypeId(String providerId, Byte itemTypeId);

    Optional<EinvMappingItemTypeEntity> findByProviderIdAndProviderItemTypeId(String providerId, String providerItemTypeId);

    List<EinvMappingItemTypeEntity> findByProviderIdAndInactiveFalse(String providerId);

    boolean existsByProviderIdAndItemTypeId(String providerId, Byte itemTypeId);
}
