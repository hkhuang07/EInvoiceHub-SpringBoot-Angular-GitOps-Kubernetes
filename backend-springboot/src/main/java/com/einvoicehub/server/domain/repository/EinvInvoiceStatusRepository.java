package com.einvoicehub.server.domain.repository;

import com.einvoicehub.server.domain.entity.EinvInvoiceStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvInvoiceStatusRepository extends JpaRepository<EinvInvoiceStatusEntity, Byte>, JpaSpecificationExecutor<EinvInvoiceStatusEntity> {

    Optional<EinvInvoiceStatusEntity> findByName(String name);

    List<EinvInvoiceStatusEntity> findByIdIn(List<Byte> ids);
}
