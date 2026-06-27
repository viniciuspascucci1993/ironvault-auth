package com.ironvault.auth.application;

import com.ironvault.auth.domain.exception.InvalidCredentialsException;
import com.ironvault.auth.domain.port.in.ChangePasswordUseCase;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ChangePasswordService implements ChangePasswordUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordService(UserRepositoryPort userRepositoryPort,
                                 PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void execute(UUID userId, String currentPassword, String newPassword) {
        var user = userRepositoryPort.findById(userId)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            log.warn("Change password failed - wrong current password userId={}", userId);
            throw new InvalidCredentialsException();
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepositoryPort.save(user);

        log.info("Password changed successfully userId={}", userId);
    }
}
