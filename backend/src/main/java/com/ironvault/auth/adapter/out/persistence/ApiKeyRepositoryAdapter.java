package com.ironvault.auth.adapter.out.persistence;

import com.ironvault.auth.adapter.out.entity.ApiKeyEntity;
import com.ironvault.auth.domain.port.out.ApiKeyRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class ApiKeyRepositoryAdapter implements ApiKeyRepositoryPort {

    private final ApiKeyJpaRepository jpaRepository;

    public ApiKeyRepositoryAdapter(ApiKeyJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(UUID userId, String key) {
        ApiKeyEntity entity = new ApiKeyEntity();

        entity.setUserId(userId);
        entity.setKey(key);
        entity.setActive(true);
        entity.setCreatedAt(Instant.now());
        jpaRepository.save(entity);
    }

    @Override
    public Optional<String> findActiveKeyByUserId(UUID userId) {
        return jpaRepository.findByUserIdAndActiveTrue(userId)
                .map(ApiKeyEntity::getKey);
    }

    @Override
    public void revoke(UUID userId) {
        jpaRepository.findByUserIdAndActiveTrue(userId)
                .ifPresent(entity -> {
                    entity.setActive(false);
                    entity.setRevokedAt(Instant.now());
                    jpaRepository.save(entity);
                });
    }

    @Override
    public boolean isValidKey(String key) {
        return jpaRepository.findByKeyAndActiveTrue(key).isPresent();
    }
}
