package com.einvoicehub.server.domain.repository;

import com.einvoicehub.server.domain.entity.EinvPaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvPaymentMethodRepository extends JpaRepository<EinvPaymentMethodEntity, Byte>, JpaSpecificationExecutor<EinvPaymentMethodEntity> {

    Optional<EinvPaymentMethodEntity> findByName(String name);

    List<EinvPaymentMethodEntity> findByIdIn(List<Byte> ids);
}
