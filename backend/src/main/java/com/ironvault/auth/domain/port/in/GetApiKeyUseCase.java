package com.ironvault.auth.domain.port.in;

import java.util.Optional;
import java.util.UUID;

public interface GetApiKeyUseCase {
    Optional<String> execute(UUID userId);
}
