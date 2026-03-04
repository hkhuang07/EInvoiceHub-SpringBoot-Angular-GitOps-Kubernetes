package vn.softz.app.einvoicehub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softz.app.einvoicehub.domain.entity.EinvHubInvoiceDetailEntity;

import java.util.List;

@Repository
public interface EinvHubInvoiceDetailRepository extends JpaRepository<EinvHubInvoiceDetailEntity, String> {

    List<EinvHubInvoiceDetailEntity> findByInvoiceIdOrderByLineNo(String invoiceId);

    void deleteByInvoiceId(String invoiceId);
}
