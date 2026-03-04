package vn.softz.app.einvoicehub.domain.repository.mapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.softz.app.einvoicehub.domain.entity.mapping.EinvMappingInvoiceTypeEntity;

import java.util.Optional;
import java.util.List;

@Repository
public interface EinvMappingInvoiceTypeRepository extends JpaRepository<EinvMappingInvoiceTypeEntity, String>,
        JpaSpecificationExecutor<EinvMappingInvoiceTypeEntity> {

    List<EinvMappingInvoiceTypeEntity> findAllByProviderIdAndInactiveFalse(String providerId);
    
    Optional<EinvMappingInvoiceTypeEntity> findByProviderIdAndInvoiceTypeId(String providerId, Integer invoiceTypeId);
}
