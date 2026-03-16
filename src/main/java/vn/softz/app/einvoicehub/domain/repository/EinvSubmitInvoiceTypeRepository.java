package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvSubmitInvoiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvSubmitInvoiceTypeRepository extends JpaRepository<EinvSubmitInvoiceTypeEntity, String>, JpaSpecificationExecutor<EinvSubmitInvoiceTypeEntity> {

    Optional<EinvSubmitInvoiceTypeEntity> findByName(String name);

    List<EinvSubmitInvoiceTypeEntity> findByIdIn(List<String> ids);
}
