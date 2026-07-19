package com.ironvault.auth.application;

import com.ironvault.auth.domain.port.in.RevokeApiKeyUseCase;
import com.ironvault.auth.domain.port.out.ApiKeyRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class RevokeApiKeyService implements RevokeApiKeyUseCase {

    private final ApiKeyRepositoryPort apiKeyRepositoryPort;

    public RevokeApiKeyService(ApiKeyRepositoryPort apiKeyRepositoryPort) {
        this.apiKeyRepositoryPort = apiKeyRepositoryPort;
    }

    @Override
    @Transactional
    public void execute(UUID userId) {
        apiKeyRepositoryPort.revoke(userId);
        log.info("API Key revoked for userId={}", userId);
    }
}
