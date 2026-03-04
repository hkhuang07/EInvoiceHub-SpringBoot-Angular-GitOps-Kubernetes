package vn.softz.app.einvoicehub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceStatusEntity;

@Repository
public interface EinvInvoiceStatusRepository extends JpaRepository<EinvInvoiceStatusEntity, Integer> {
}
