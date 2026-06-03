package com.ironvault.auth.adapter.out.persistence;

import com.ironvault.auth.adapter.out.entity.EmailConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailConfirmationTokenJpaRepository extends JpaRepository<EmailConfirmationTokenEntity, UUID> {
    Optional<EmailConfirmationTokenEntity> findByToken(String token);
}
