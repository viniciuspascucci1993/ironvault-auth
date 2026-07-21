package com.ironvault.auth.domain.port.in;

import java.util.UUID;

public interface RejectUserUseCase {
    void execute(UUID userId);
}
