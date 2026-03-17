package vn.softz.app.einvoicehub.service.integration;

import vn.softz.app.einvoicehub.domain.entity.MerchantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MerchantService {

    MerchantEntity create(MerchantEntity merchant);

    MerchantEntity update(Long id, MerchantEntity merchant);

    void deactivate(Long id);

    MerchantEntity activate(Long id);

    Optional<MerchantEntity> findById(Long id);

    Optional<MerchantEntity> findByTenantId(String tenantId);

    Optional<MerchantEntity> findByTaxCode(String taxCode);

    List<MerchantEntity> findAllActive();

    Page<MerchantEntity> findAllActive(Pageable pageable);

    Page<MerchantEntity> searchByName(String companyName, Pageable pageable);

    boolean existsByTenantId(String tenantId);

    boolean existsByTaxCode(String taxCode);
}
