package com.ironvault.auth.domain.port.in;

import com.ironvault.auth.domain.enums.Role;

public interface RegisterUserUseCase {

    void execute(String email, String password, Role role, String source);
}
