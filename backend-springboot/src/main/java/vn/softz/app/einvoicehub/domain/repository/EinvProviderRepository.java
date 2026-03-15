package vn.softz.app.einvoicehub.domain.repository;

import vn.softz.app.einvoicehub.domain.entity.EinvProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvProviderRepository extends JpaRepository<EinvProviderEntity, String>, JpaSpecificationExecutor<EinvProviderEntity> {

    Optional<EinvProviderEntity> findByProviderCode(String providerCode);

    List<EinvProviderEntity> findByInactiveFalse();

    boolean existsByProviderCode(String providerCode);

    List<EinvProviderEntity> findByIdIn(List<String> ids);
}
