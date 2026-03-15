package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvMappingUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvMappingUnitRepository extends JpaRepository<EinvMappingUnitEntity, String>, JpaSpecificationExecutor<EinvMappingUnitEntity> {

    /**
     * Tìm mapping theo provider ID
     */
    List<EinvMappingUnitEntity> findByProviderId(String providerId);

    /**
     * Tìm mapping theo mã đơn vị tính
     */
    List<EinvMappingUnitEntity> findByUnitCode(String unitCode);

    /**
     * Tìm mapping theo provider và mã đơn vị tính HUB
     */
    Optional<EinvMappingUnitEntity> findByProviderIdAndUnitCode(String providerId, String unitCode);

    /**
     * Tìm mapping theo provider và mã đơn vị tính NCC
     */
    Optional<EinvMappingUnitEntity> findByProviderIdAndProviderUnitCode(String providerId, String providerUnitCode);

    /**
     * Tìm mapping đang hoạt động theo provider
     */
    List<EinvMappingUnitEntity> findByProviderIdAndInactiveFalse(String providerId);

    /**
     * Kiểm tra tồn tại theo provider và mã đơn vị tính HUB
     */
    boolean existsByProviderIdAndUnitCode(String providerId, String unitCode);
}
