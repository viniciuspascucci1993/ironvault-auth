package com.ironvault.auth.application;

import com.ironvault.auth.domain.enums.Role;
import com.ironvault.auth.domain.exception.UserAlreadyExistsException;
import com.ironvault.auth.domain.model.User;
import com.ironvault.auth.domain.port.in.RegisterUserUseCase;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserService(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(String email, String password, Role role) {

        if (userRepositoryPort.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }

        log.info("Criando Usuário...{}", email);

        User user = User.create(email, passwordEncoder.encode(password), role);
        userRepositoryPort.save(user);

    }
}
