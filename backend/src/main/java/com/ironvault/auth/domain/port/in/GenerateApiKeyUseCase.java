package com.ironvault.auth.domain.port.in;

import java.util.UUID;

public interface GenerateApiKeyUseCase {
    String execute(UUID userId);
}
