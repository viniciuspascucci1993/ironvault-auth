package com.ironvault.auth.domain.port.in;

import java.util.UUID;

public interface UpdateUserStatusUseCase {
    void execute(UUID id, boolean active);
}
