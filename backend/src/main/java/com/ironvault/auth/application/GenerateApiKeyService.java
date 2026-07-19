package com.ironvault.auth.application;

import com.ironvault.auth.domain.port.in.GenerateApiKeyUseCase;
import com.ironvault.auth.domain.port.out.ApiKeyRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
@Slf4j
public class GenerateApiKeyService implements GenerateApiKeyUseCase {

    private final ApiKeyRepositoryPort apiKeyRepositoryPort;

    public GenerateApiKeyService(ApiKeyRepositoryPort apiKeyRepositoryPort) {
        this.apiKeyRepositoryPort = apiKeyRepositoryPort;
    }

    @Override
    @Transactional
    public String execute(UUID userId) {

        // Revoga a key anterior caso exista
        apiKeyRepositoryPort.revoke(userId);

        // Gera nova API key
        String key = generateKey();
        apiKeyRepositoryPort.save(userId, key);

        log.info("API Key generated for userId={}", userId);
        return key;
    }

    private String generateKey() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return "iv_live_" + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
