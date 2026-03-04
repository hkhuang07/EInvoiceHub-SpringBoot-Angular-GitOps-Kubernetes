package vn.softz.app.einvoicehub.domain.repository.mapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.softz.app.einvoicehub.domain.entity.mapping.EinvMappingTaxTypeEntity;

import java.util.Optional;
import java.util.List;

@Repository
public interface EinvMappingTaxTypeRepository extends JpaRepository<EinvMappingTaxTypeEntity, String>,
        JpaSpecificationExecutor<EinvMappingTaxTypeEntity> {

    List<EinvMappingTaxTypeEntity> findAllByProviderIdAndInactiveFalse(String providerId);

    Optional<EinvMappingTaxTypeEntity> findByProviderIdAndTaxTypeId(String providerId, String taxTypeId);
}
