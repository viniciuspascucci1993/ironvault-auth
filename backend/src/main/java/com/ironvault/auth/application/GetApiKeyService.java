package com.ironvault.auth.application;

import com.ironvault.auth.domain.port.in.GetApiKeyUseCase;
import com.ironvault.auth.domain.port.out.ApiKeyRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetApiKeyService implements GetApiKeyUseCase {

    private final ApiKeyRepositoryPort apiKeyRepositoryPort;

    public GetApiKeyService(ApiKeyRepositoryPort apiKeyRepositoryPort) {
        this.apiKeyRepositoryPort = apiKeyRepositoryPort;
    }

    @Override
    public Optional<String> execute(UUID userId) {
        return apiKeyRepositoryPort.findActiveKeyByUserId(userId);
    }
}
