package com.ironvault.auth.domain.port.in;

import com.ironvault.auth.domain.enums.Role;

import java.util.UUID;

public interface UpdateUserRoleUseCase {
    void execute(UUID id, Role role);
}
