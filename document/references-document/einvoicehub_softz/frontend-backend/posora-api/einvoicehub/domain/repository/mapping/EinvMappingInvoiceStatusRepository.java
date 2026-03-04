package vn.softz.app.einvoicehub.domain.repository.mapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.softz.app.einvoicehub.domain.entity.mapping.EinvMappingInvoiceStatusEntity;

import java.util.Optional;
import java.util.List;

@Repository
public interface EinvMappingInvoiceStatusRepository extends JpaRepository<EinvMappingInvoiceStatusEntity, String>,
        JpaSpecificationExecutor<EinvMappingInvoiceStatusEntity> {

    List<EinvMappingInvoiceStatusEntity> findAllByProviderIdAndInactiveFalse(String providerId);

    Optional<EinvMappingInvoiceStatusEntity> findByProviderIdAndInvoiceStatusId(String providerId, Integer invoiceStatusId);
}
