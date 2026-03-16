package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvMappingReferenceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvMappingReferenceTypeRepository extends JpaRepository<EinvMappingReferenceTypeEntity, String>, JpaSpecificationExecutor<EinvMappingReferenceTypeEntity> {

    List<EinvMappingReferenceTypeEntity> findByProviderId(String providerId);

    List<EinvMappingReferenceTypeEntity> findByReferenceTypeId(Byte referenceTypeId);

    Optional<EinvMappingReferenceTypeEntity> findByProviderIdAndReferenceTypeId(String providerId, Byte referenceTypeId);

    Optional<EinvMappingReferenceTypeEntity> findByProviderIdAndProviderReferenceTypeId(String providerId, String providerReferenceTypeId);

    List<EinvMappingReferenceTypeEntity> findByProviderIdAndInactiveFalse(String providerId);

    boolean existsByProviderIdAndReferenceTypeId(String providerId, Byte referenceTypeId);
}
