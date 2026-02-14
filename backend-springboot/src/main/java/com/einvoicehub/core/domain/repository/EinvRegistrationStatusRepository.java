package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvRegistrationStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EinvRegistrationStatusRepository extends JpaRepository<EinvRegistrationStatusEntity, Integer>,
        JpaSpecificationExecutor<EinvRegistrationStatusEntity> {

    Optional<EinvRegistrationStatusEntity> findByName(String name);

    @Query("SELECT r FROM EinvRegistrationStatusEntity r WHERE " +
            "(:keyword IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<EinvRegistrationStatusEntity> searchByKeyword(@Param("keyword") String keyword);

    boolean existsById(Integer id);
}