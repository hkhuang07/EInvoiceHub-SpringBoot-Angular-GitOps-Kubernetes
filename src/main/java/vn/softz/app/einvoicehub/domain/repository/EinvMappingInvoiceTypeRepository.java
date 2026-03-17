package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvMappingInvoiceStatusEntity;
import vn.softz.app.einvoicehub.domain.entity.EinvMappingInvoiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvMappingInvoiceTypeRepository extends JpaRepository<EinvMappingInvoiceTypeEntity, String>, JpaSpecificationExecutor<EinvMappingInvoiceTypeEntity> {

    List<EinvMappingInvoiceTypeEntity> findByProviderId(String providerId);

    List<EinvMappingInvoiceTypeEntity> findByInvoiceTypeId(Byte invoiceTypeId);

    Optional<EinvMappingInvoiceTypeEntity> findByProviderIdAndInvoiceTypeId(String providerId, Byte invoiceTypeId);

    Optional<EinvMappingInvoiceTypeEntity> findByProviderIdAndProviderInvoiceTypeId(String providerId, String providerInvoiceTypeId);

    List<EinvMappingInvoiceTypeEntity> findByProviderIdAndInactiveFalse(String providerId);

    List<EinvMappingInvoiceTypeEntity> findAllByProviderIdAndInactiveFalse(String provderId);

    boolean existsByProviderIdAndInvoiceTypeId(String providerId, Byte invoiceTypeId);

}
