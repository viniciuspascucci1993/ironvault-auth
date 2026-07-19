package com.ironvault.auth.domain.port.out;

import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepositoryPort {
    void save(UUID userId, String key);
    Optional<String> findActiveKeyByUserId(UUID userId);
    void revoke(UUID userId);
    boolean isValidKey(String key);
}
