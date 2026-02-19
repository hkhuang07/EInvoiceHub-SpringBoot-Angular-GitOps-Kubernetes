package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvInvoiceRegistrationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvInvoiceRegistrationRepository extends JpaRepository<EinvInvoiceRegistrationEntity, Long>,
        JpaSpecificationExecutor<EinvInvoiceRegistrationEntity> {

    @EntityGraph(attributePaths = {"status"})
    List<EinvInvoiceRegistrationEntity> findByMerchantIdAndStatusIdOrderByEffectiveDateDesc(Long merchantId, Integer statusId);

    Optional<EinvInvoiceRegistrationEntity> findByRegistrationNumber(String registrationNumber);

    @Query(value = "SELECT * FROM invoice_registrations WHERE merchant_id = :merchantId " +
            "AND status_id = 1 ORDER BY effective_date DESC LIMIT 1", nativeQuery = true)
    Optional<EinvInvoiceRegistrationEntity> findLatestActiveRegistration(@Param("merchantId") Long merchantId);

    boolean existsByStatusId(Integer statusId);
}