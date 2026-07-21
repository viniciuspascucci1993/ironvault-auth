package com.ironvault.auth.domain.port.in;

import java.util.UUID;

public interface ApproveUserUseCase {
    void execute(UUID userId);
}
