package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvStoreSerialEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvStoreSerialRepository extends JpaRepository<EinvStoreSerialEntity, String>, JpaSpecificationExecutor<EinvStoreSerialEntity> {

    List<EinvStoreSerialEntity> findByTenantId(String tenantId);

    List<EinvStoreSerialEntity> findByStoreId(String storeId);

    List<EinvStoreSerialEntity> findByProviderId(String providerId);

    List<EinvStoreSerialEntity> findByStoreIdAndProviderId(String storeId, String providerId);

    Optional<EinvStoreSerialEntity> findByStoreIdAndProviderIdAndInvoiceTypeId(String storeId, String providerId, Byte invoiceTypeId);

    Optional<EinvStoreSerialEntity> findByInvoiceForm(String invoiceForm);

    Optional<EinvStoreSerialEntity> findByInvoiceSerial(String invoiceSerial);

    List<EinvStoreSerialEntity> findByStatus(Byte status);

    Page<EinvStoreSerialEntity> findByStatus(Byte status, Pageable pageable);

    List<EinvStoreSerialEntity> findByTenantIdAndStatus(String tenantId, Byte status);

    boolean existsByStoreIdAndStatus(String storeId, Byte status);

    boolean existsByStoreIdAndProviderIdAndInvoiceTypeId(String storeId, String providerId, Byte invoiceTypeId);

    long countByStoreId(String storeId);
}
