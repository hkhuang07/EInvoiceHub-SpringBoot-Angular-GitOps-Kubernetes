package com.einvoicehub.server.domain.repository;

import com.einvoicehub.server.domain.entity.EinvReceiveTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvReceiveTypeRepository extends JpaRepository<EinvReceiveTypeEntity, Byte>, JpaSpecificationExecutor<EinvReceiveTypeEntity> {

    Optional<EinvReceiveTypeEntity> findByName(String name);

    List<EinvReceiveTypeEntity> findByIdIn(List<Byte> ids);
}
