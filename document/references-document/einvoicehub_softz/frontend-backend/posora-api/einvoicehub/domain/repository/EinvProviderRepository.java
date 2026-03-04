package vn.softz.app.einvoicehub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softz.app.einvoicehub.domain.entity.EinvProviderEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvProviderRepository extends JpaRepository<EinvProviderEntity, String> {
    Optional<EinvProviderEntity> findByIdAndInactiveFalse(String id);
    
    List<EinvProviderEntity> findByInactiveFalse();
}
