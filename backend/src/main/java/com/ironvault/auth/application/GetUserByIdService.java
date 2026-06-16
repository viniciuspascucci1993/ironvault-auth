package com.ironvault.auth.application;

import com.ironvault.auth.domain.exception.UserNotFoundException;
import com.ironvault.auth.domain.model.User;
import com.ironvault.auth.domain.port.in.GetUserByIdUseCase;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class GetUserByIdService implements GetUserByIdUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public GetUserByIdService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User execute(UUID id) {
        log.info("Get user id={}", id);
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }
}
