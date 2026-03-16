package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvItemTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvItemTypeRepository extends JpaRepository<EinvItemTypeEntity, Byte>, JpaSpecificationExecutor<EinvItemTypeEntity> {

    Optional<EinvItemTypeEntity> findByName(String name);

    List<EinvItemTypeEntity> findByIdIn(List<Byte> ids);

    List<EinvItemTypeEntity> findAll();
}
