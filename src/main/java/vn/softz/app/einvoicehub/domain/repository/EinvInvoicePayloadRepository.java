package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvInvoicePayloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvInvoicePayloadRepository extends JpaRepository<EinvInvoicePayloadEntity, String>, JpaSpecificationExecutor<EinvInvoicePayloadEntity> {

    Optional<EinvInvoicePayloadEntity> findByInvoiceId(String invoiceId);

    boolean existsByInvoiceId(String invoiceId);

    void deleteByInvoiceId(String invoiceId);
}
