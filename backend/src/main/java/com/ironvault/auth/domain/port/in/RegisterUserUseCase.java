package com.ironvault.auth.domain.port.in;

import com.ironvault.auth.domain.enums.Role;

import java.util.UUID;

public interface RegisterUserUseCase {

    UUID execute(String email, String password, Role role, String source);
}
