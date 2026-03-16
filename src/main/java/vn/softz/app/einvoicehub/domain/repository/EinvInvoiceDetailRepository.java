package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EinvInvoiceDetailRepository extends JpaRepository<EinvInvoiceDetailEntity, String>, JpaSpecificationExecutor<EinvInvoiceDetailEntity> {

    List<EinvInvoiceDetailEntity> findAllByInvoiceId(String invoiceId);

    List<EinvInvoiceDetailEntity> findAllByDocId(String docId);

    List<EinvInvoiceDetailEntity> findByDocIdAndLineNo(String docId, Integer lineNo);

    void deleteAllByDocId(String docId);

    void deleteAllByInvoiceId(String invoiceId);

    long countByDocId(String docId);

    List<EinvInvoiceDetailEntity> findByItemTypeId(Byte itemTypeId);

    List<EinvInvoiceDetailEntity> findByItemId(String itemId);

}
