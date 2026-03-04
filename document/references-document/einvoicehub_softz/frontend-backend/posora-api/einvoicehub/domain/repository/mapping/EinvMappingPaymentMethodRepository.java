package vn.softz.app.einvoicehub.domain.repository.mapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.softz.app.einvoicehub.domain.entity.mapping.EinvMappingPaymentMethodEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvMappingPaymentMethodRepository extends JpaRepository<EinvMappingPaymentMethodEntity, String>,
        JpaSpecificationExecutor<EinvMappingPaymentMethodEntity> {
        
    List<EinvMappingPaymentMethodEntity> findAllByProviderIdAndInactiveFalse(String providerId);        

    Optional<EinvMappingPaymentMethodEntity> findByProviderIdAndPaymentMethodId(String providerId, Integer paymentMethodId);
}
