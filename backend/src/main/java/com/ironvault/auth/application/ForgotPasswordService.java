package com.ironvault.auth.application;

import com.ironvault.auth.adapter.out.client.NotificationClient;
import com.ironvault.auth.domain.model.PasswordResetToken;
import com.ironvault.auth.domain.port.in.ForgotPasswordUseCase;
import com.ironvault.auth.domain.port.out.PasswordResetTokenRepositoryPort;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ForgotPasswordService implements ForgotPasswordUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordResetTokenRepositoryPort tokenRepositoryPort;
    private final NotificationClient notificationClient;

    @Value("${app.auth.confirmation-url:https://auth.ironvaultpayments.com.br}")
    private String baseUrl;

    public ForgotPasswordService(UserRepositoryPort userRepositoryPort,
                                 PasswordResetTokenRepositoryPort tokenRepositoryPort,
                                 NotificationClient notificationClient) {
        this.userRepositoryPort = userRepositoryPort;
        this.tokenRepositoryPort = tokenRepositoryPort;
        this.notificationClient = notificationClient;
    }

    @Override
    @Transactional
    public void execute(String email) {
        // Se o email não existe, não revelamos — segurança
        var user = userRepositoryPort.findByEmail(email);
        if (user.isEmpty()) {
            log.warn("Password reset requested for non-existent email={}", email);
            return;
        }

        PasswordResetToken token = PasswordResetToken.create(user.get().getId());
        tokenRepositoryPort.save(token);

        String resetLink = baseUrl + "/api/auth/reset-password?token=" + token.getToken();
        notificationClient.sendPasswordResetEvent(email, resetLink);

        log.info("Password reset token generated userId={}", user.get().getId());
    }
}
