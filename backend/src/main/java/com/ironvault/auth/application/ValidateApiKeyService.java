package com.ironvault.auth.application;

import com.ironvault.auth.domain.port.in.ValidateApiKeyUseCase;
import com.ironvault.auth.domain.port.out.ApiKeyRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class ValidateApiKeyService implements ValidateApiKeyUseCase {

    private final ApiKeyRepositoryPort apiKeyRepositoryPort;

    public ValidateApiKeyService(ApiKeyRepositoryPort apiKeyRepositoryPort) {
        this.apiKeyRepositoryPort = apiKeyRepositoryPort;
    }

    @Override
    public boolean execute(String apiKey) {
        return apiKeyRepositoryPort.isValidKey(apiKey);
    }
}
