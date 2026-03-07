package com.einvoicehub.server.domain.repository;

import com.einvoicehub.server.domain.entity.EinvUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvUnitRepository extends JpaRepository<EinvUnitEntity, String>, JpaSpecificationExecutor<EinvUnitEntity> {

    Optional<EinvUnitEntity> findByCode(String code);

    Optional<EinvUnitEntity> findByName(String name);

    List<EinvUnitEntity> findByCodeIn(List<String> codes);

    boolean existsByCode(String code);
}
