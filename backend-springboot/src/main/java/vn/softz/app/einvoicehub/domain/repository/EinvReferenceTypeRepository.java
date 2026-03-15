package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvReferenceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvReferenceTypeRepository extends JpaRepository<EinvReferenceTypeEntity, Byte>, JpaSpecificationExecutor<EinvReferenceTypeEntity> {

    Optional<EinvReferenceTypeEntity> findByName(String name);

    List<EinvReferenceTypeEntity> findByIdIn(List<Byte> ids);
}
