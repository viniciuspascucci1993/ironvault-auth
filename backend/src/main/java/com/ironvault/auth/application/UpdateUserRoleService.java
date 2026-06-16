package com.ironvault.auth.application;

import com.ironvault.auth.domain.enums.Role;
import com.ironvault.auth.domain.exception.UserNotFoundException;
import com.ironvault.auth.domain.port.in.UpdateUserRoleUseCase;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UpdateUserRoleService implements UpdateUserRoleUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UpdateUserRoleService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public void execute(UUID id, Role role) {
        var user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
        user.setRole(role);
        userRepositoryPort.save(user);
        log.info("User role updated id={} role={}", id, role);
    }
}
