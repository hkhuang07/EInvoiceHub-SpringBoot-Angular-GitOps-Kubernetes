package vn.softz.app.einvoicehub.domain.repository.mapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.softz.app.einvoicehub.domain.entity.mapping.EinvMappingItemTypeEntity;

import java.util.Optional;
import java.util.List;

@Repository
public interface EinvMappingItemTypeRepository extends JpaRepository<EinvMappingItemTypeEntity, String>,
        JpaSpecificationExecutor<EinvMappingItemTypeEntity> {

    List<EinvMappingItemTypeEntity> findAllByProviderIdAndInactiveFalse(String providerId);

    Optional<EinvMappingItemTypeEntity> findByProviderIdAndItemTypeId(String providerId, Integer itemTypeId);
}
