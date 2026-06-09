package com.ironvault.auth.application;

import com.ironvault.auth.domain.exception.InvalidCredentialsException;
import com.ironvault.auth.domain.port.in.ResetPasswordUseCase;
import com.ironvault.auth.domain.port.out.PasswordResetTokenRepositoryPort;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResetPasswordService implements ResetPasswordUseCase {

    private final PasswordResetTokenRepositoryPort tokenRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordService(PasswordResetTokenRepositoryPort tokenRepositoryPort,
                                UserRepositoryPort userRepositoryPort,
                                PasswordEncoder passwordEncoder) {
        this.tokenRepositoryPort = tokenRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void execute(String token, String newPassword) {
        var resetToken = tokenRepositoryPort.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Password reset token not found token={}", token);
                    return new InvalidCredentialsException();
                });

        if (resetToken.isUsed() || resetToken.isExpired()) {
            log.warn("Password reset token invalid userId={}", resetToken.getUserId());
            throw new InvalidCredentialsException();
        }

        var user = userRepositoryPort.findById(resetToken.getUserId())
                .orElseThrow(InvalidCredentialsException::new);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepositoryPort.save(user);

        resetToken.setUsed(true);
        tokenRepositoryPort.save(resetToken);

        log.info("Password reset successful userId={}", user.getId());
    }
}
