package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvVatRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvVatRateRepository extends JpaRepository<EinvVatRateEntity, Long>,
        JpaSpecificationExecutor<EinvVatRateEntity> {

    Optional<EinvVatRateEntity> findByRateCode(String rateCode);

    List<EinvVatRateEntity> findByIsActiveTrueOrderByDisplayOrderAsc();

    @Query("SELECT v FROM EinvVatRateEntity v WHERE " +
            "(:search IS NULL OR LOWER(v.rateCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(v.rateName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND v.isActive = true " +
            "ORDER BY v.displayOrder ASC")
    List<EinvVatRateEntity> searchActiveVatRates(@Param("search") String search);

    @Query(value = "SELECT * FROM vat_rates v WHERE v.is_active = 1 " +
            "ORDER BY v.rate_percent DESC LIMIT 1", nativeQuery = true)
    Optional<EinvVatRateEntity> findDefaultMaxTaxRate();
}