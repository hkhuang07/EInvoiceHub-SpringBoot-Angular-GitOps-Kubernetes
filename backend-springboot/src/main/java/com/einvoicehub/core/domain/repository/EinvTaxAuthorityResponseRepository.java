package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvTaxAuthorityResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvTaxAuthorityResponseRepository extends JpaRepository<EinvTaxAuthorityResponseEntity, Long>,
        JpaSpecificationExecutor<EinvTaxAuthorityResponseEntity> {

    Optional<EinvTaxAuthorityResponseEntity> findByCqtCode(String cqtCode);

     //Lấy thông tin phản hồi kèm dữ liệu hóa đơn cha.
    @EntityGraph(attributePaths = {"invoice"})
    Optional<EinvTaxAuthorityResponseEntity> findByInvoiceId(Long invoiceId);

    @Query("SELECT r FROM EinvTaxAuthorityResponseEntity r WHERE " +
            "(:cqtCode IS NULL OR r.cqtCode = :cqtCode) AND " +
            "(:search IS NULL OR LOWER(r.errorMessage) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(r.statusFromCqt) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<EinvTaxAuthorityResponseEntity> searchResponses(
            @Param("cqtCode") String cqtCode,
            @Param("search") String search,
            Pageable pageable
    );
}