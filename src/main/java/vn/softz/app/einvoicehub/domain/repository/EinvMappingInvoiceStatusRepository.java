package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvMappingInvoiceStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvMappingInvoiceStatusRepository extends JpaRepository<EinvMappingInvoiceStatusEntity, String>, JpaSpecificationExecutor<EinvMappingInvoiceStatusEntity> {

    List<EinvMappingInvoiceStatusEntity> findByProviderId(String providerId);

    List<EinvMappingInvoiceStatusEntity> findByInvoiceStatusId(Byte invoiceStatusId);

    Optional<EinvMappingInvoiceStatusEntity> findByProviderIdAndInvoiceStatusId(String providerId, Byte invoiceStatusId);

    Optional<EinvMappingInvoiceStatusEntity> findByProviderIdAndProviderInvoiceStatusId(String providerId, String providerInvoiceStatusId);

    List<EinvMappingInvoiceStatusEntity> findByProviderIdAndInactiveFalse(String providerId);

    boolean existsByProviderIdAndInvoiceStatusId(String providerId, Byte invoiceStatusId);

    List<EinvMappingInvoiceStatusEntity> findAllByProviderIdAndInactiveFalse(String providerId);

}
