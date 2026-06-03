package com.ironvault.auth.application;

import com.ironvault.auth.adapter.out.persistence.EmailConfirmationTokenJpaRepository;
import com.ironvault.auth.domain.exception.InvalidCredentialsException;
import com.ironvault.auth.domain.port.in.ConfirmationEmailUseCase;
import com.ironvault.auth.domain.port.out.EmailConfirmationTokenRepositoryPort;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConfirmationEmailService implements ConfirmationEmailUseCase {

    private final EmailConfirmationTokenRepositoryPort repositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public ConfirmationEmailService(EmailConfirmationTokenRepositoryPort repositoryPort,
                                    UserRepositoryPort userRepositoryPort) {
        this.repositoryPort = repositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public void confirm(String token) {
        var confirmationToken = repositoryPort.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Confirmation token not found token={}", token);
                    return new InvalidCredentialsException();
                });

        if (confirmationToken.isUsed() || confirmationToken.isExpired()) {
            log.warn("Confirmation token invalid userId={}", confirmationToken.getUserId());
            throw new InvalidCredentialsException();
        }

        var user = userRepositoryPort.findById(confirmationToken.getUserId())
                .orElseThrow(InvalidCredentialsException::new);

        user.setEmailConfirmed(true);
        userRepositoryPort.save(user);

        confirmationToken.setUsed(true);
        repositoryPort.save(confirmationToken);

        log.info("Email confirmed userId={}", user.getId());

    }
}
