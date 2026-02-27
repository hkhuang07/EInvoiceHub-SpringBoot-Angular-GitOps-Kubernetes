package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvTaxStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EinvTaxStatusRepository extends JpaRepository<EinvTaxStatusEntity, Integer>, JpaSpecificationExecutor<EinvTaxStatusEntity> {


}