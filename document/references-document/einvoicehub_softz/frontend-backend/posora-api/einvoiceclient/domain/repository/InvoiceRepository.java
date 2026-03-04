package vn.softz.app.einvoiceclient.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.softz.app.biz.invoice.domain.entity.InvoiceEntity;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, String>, JpaSpecificationExecutor<InvoiceEntity> {
    
    Optional<InvoiceEntity> findByInvoiceGuid(String invoiceGuid);

    Optional<InvoiceEntity> findByDocId(String docId);

    boolean existsByDocId(String docId);
}
