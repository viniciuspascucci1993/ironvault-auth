package com.ironvault.auth.application;

import com.ironvault.auth.domain.model.User;
import com.ironvault.auth.domain.port.in.GetAllUsersUseCase;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GetAllUsersService implements GetAllUsersUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public GetAllUsersService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public List<User> execute() {
        log.info("Get all users");
        return userRepositoryPort.findAll();
    }
}
