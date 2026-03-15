package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvInvoiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvInvoiceTypeRepository extends JpaRepository<EinvInvoiceTypeEntity, Byte>, JpaSpecificationExecutor<EinvInvoiceTypeEntity> {

    Optional<EinvInvoiceTypeEntity> findByName(String name);

    List<EinvInvoiceTypeEntity> findByIdIn(List<Byte> ids);
}
