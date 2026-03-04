package vn.softz.app.einvoicehub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softz.app.einvoicehub.domain.entity.EinvUserAuthorEntity;

@Repository
public interface EinvUserAuthorRepository extends JpaRepository<EinvUserAuthorEntity, String> {
}
