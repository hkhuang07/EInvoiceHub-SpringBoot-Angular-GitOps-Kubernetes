package vn.softz.app.einvoicehub.domain.repository.mapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.softz.app.einvoicehub.domain.entity.mapping.EinvMappingReferenceTypeEntity;

import java.util.Optional;
import java.util.List;

@Repository
public interface EinvMappingReferenceTypeRepository extends JpaRepository<EinvMappingReferenceTypeEntity, String>,
        JpaSpecificationExecutor<EinvMappingReferenceTypeEntity> {

    List<EinvMappingReferenceTypeEntity> findAllByProviderIdAndInactiveFalse(String providerId);
    
    Optional<EinvMappingReferenceTypeEntity> findByProviderIdAndReferenceTypeId(String providerId, Integer referenceTypeId);
}
