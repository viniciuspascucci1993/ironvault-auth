package com.ironvault.auth.domain.port.in;

import java.util.UUID;

public interface RevokeApiKeyUseCase {
    void execute(UUID userId);
}
