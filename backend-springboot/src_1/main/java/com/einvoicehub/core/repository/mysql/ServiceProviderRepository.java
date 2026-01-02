package com.einvoicehub.core.repository.mysql;

import com.einvoicehub.core.entity.mysql.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

    Optional<ServiceProvider> findByProviderCode(String providerCode);

    List<ServiceProvider> findByIsActiveTrue();

    Optional<ServiceProvider> findByIsActiveTrueAndIsDefaultTrue();

    List<ServiceProvider> findByIsActiveTrueOrderByDisplayOrderAsc();

    @Query("SELECT s FROM ServiceProvider s WHERE s.isActive = true ORDER BY s.displayOrder ASC")
    List<ServiceProvider> findActiveProvidersOrdered();

    @Query("SELECT s FROM ServiceProvider s WHERE s.providerCode IN :codes")
    List<ServiceProvider> findByProviderCodes(@Param("codes") List<String> codes);

    boolean existsByProviderCode(String providerCode);
}