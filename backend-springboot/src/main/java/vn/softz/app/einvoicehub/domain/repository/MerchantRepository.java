package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.MerchantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<MerchantEntity, Long>, JpaSpecificationExecutor<MerchantEntity> {

    Optional<MerchantEntity> findByTenantId(String tenantId);

    Optional<MerchantEntity> findByTaxCode(String taxCode);

    boolean existsByTenantId(String tenantId);
    boolean existsByTaxCode(String taxCode);

    List<MerchantEntity> findByIsActiveTrue();

    Page<MerchantEntity> findByIsActiveTrue(Pageable pageable);

    List<MerchantEntity> findByCompanyNameContainingIgnoreCase(String companyName);

    Page<MerchantEntity> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);
}
