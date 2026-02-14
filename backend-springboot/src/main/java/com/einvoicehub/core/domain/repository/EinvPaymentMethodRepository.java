package com.einvoicehub.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.einvoicehub.core.domain.entity.EinvPaymentMethodEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvPaymentMethodRepository extends JpaRepository<EinvPaymentMethodEntity, Long>,
        JpaSpecificationExecutor<EinvPaymentMethodEntity> {

    List<EinvPaymentMethodEntity> findByIsActiveTrueOrderByDisplayOrderAsc();

    Optional<EinvPaymentMethodEntity> findByMethodCode(String methodCode);

    List<EinvPaymentMethodEntity> findAllByIdIn(List<Long> ids);

    @Query("SELECT p FROM EinvPaymentMethodEntity p WHERE " +
            "(:search IS NULL OR LOWER(p.methodCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.methodName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND p.isActive = true")
    List<EinvPaymentMethodEntity> searchActiveMethods(@Param("search") String search);
}