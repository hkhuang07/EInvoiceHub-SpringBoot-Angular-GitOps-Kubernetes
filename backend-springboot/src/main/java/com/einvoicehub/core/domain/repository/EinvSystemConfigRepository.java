package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.EinvSystemConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EinvSystemConfigRepository extends JpaRepository<EinvSystemConfigEntity, Long>,
        JpaSpecificationExecutor<EinvSystemConfigEntity> {

    Optional<EinvSystemConfigEntity> findByConfigKey(String configKey);

    @Query(value = "SELECT config_value FROM system_config WHERE config_key = :key", nativeQuery = true)
    String getValueByKey(@Param("key") String key);

    boolean existsByConfigKeyAndIsEditableTrue(String configKey);
}