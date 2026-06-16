package com.ironvault.auth.application;

import com.ironvault.auth.domain.exception.UserNotFoundException;
import com.ironvault.auth.domain.port.in.UpdateUserStatusUseCase;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UpdateUserStatusService implements UpdateUserStatusUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UpdateUserStatusService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public void execute(UUID id, boolean active) {
        var user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
        user.setActive(active);
        userRepositoryPort.save(user);
        log.info("User status updated id={} active={}", id, active);
    }
}
