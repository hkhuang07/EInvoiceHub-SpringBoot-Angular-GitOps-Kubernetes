package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvTaxStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvTaxStatusRepository extends JpaRepository<EinvTaxStatusEntity, Byte>, JpaSpecificationExecutor<EinvTaxStatusEntity> {

    Optional<EinvTaxStatusEntity> findByName(String name);

    List<EinvTaxStatusEntity> findByIdIn(List<Byte> ids);

    List<EinvTaxStatusEntity> findAll();
}
