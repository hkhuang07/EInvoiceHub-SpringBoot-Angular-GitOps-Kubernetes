package com.einvoicehub.core.domain.repository;

import com.einvoicehub.core.domain.entity.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUserEntity, Long>,
        JpaSpecificationExecutor<SysUserEntity> {

    Optional<SysUserEntity> findByName(String name);
}