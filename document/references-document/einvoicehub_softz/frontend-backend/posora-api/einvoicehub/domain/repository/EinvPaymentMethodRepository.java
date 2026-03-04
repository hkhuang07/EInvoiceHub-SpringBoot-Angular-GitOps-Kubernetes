package vn.softz.app.einvoicehub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softz.app.einvoicehub.domain.entity.EinvPaymentMethodEntity;

import java.util.List;

@Repository
public interface EinvPaymentMethodRepository extends JpaRepository<EinvPaymentMethodEntity, Integer> {
    List<EinvPaymentMethodEntity> findAllByIdIn(List<Integer> ids);
}
