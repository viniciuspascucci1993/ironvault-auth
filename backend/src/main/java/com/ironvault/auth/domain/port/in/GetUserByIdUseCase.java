package com.ironvault.auth.domain.port.in;

import com.ironvault.auth.domain.model.User;

import java.util.UUID;

public interface GetUserByIdUseCase {
    User execute(UUID id);
}
