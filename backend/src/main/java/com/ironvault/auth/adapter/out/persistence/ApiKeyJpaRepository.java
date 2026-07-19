package com.ironvault.auth.adapter.out.persistence;

import com.ironvault.auth.adapter.out.entity.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApiKeyJpaRepository extends JpaRepository<ApiKeyEntity, UUID> {
    Optional<ApiKeyEntity> findByKeyAndActiveTrue(String key);
    Optional<ApiKeyEntity> findByUserIdAndActiveTrue(UUID userId);
}
