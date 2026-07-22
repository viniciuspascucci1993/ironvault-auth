package com.ironvault.auth.application;

import com.ironvault.auth.adapter.out.client.NotificationClient;
import com.ironvault.auth.domain.enums.Role;
import com.ironvault.auth.domain.exception.UserAlreadyExistsException;
import com.ironvault.auth.domain.model.EmailConfirmationToken;
import com.ironvault.auth.domain.model.User;
import com.ironvault.auth.domain.port.in.ForgotPasswordUseCase;
import com.ironvault.auth.domain.port.in.RegisterUserUseCase;
import com.ironvault.auth.domain.port.out.EmailConfirmationTokenRepositoryPort;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final EmailConfirmationTokenRepositoryPort emailConfirmationTokenRepositoryPort;
    private final NotificationClient notificationClient;
    private final ForgotPasswordUseCase forgotPasswordUseCase;

    @Value("${app.auth.confirmation-url:https://auth.ironvaultpayments.com.br}")
    private String confirmationBaseUrl;

    public RegisterUserService(UserRepositoryPort userRepositoryPort,
                               PasswordEncoder passwordEncoder,
                               EmailConfirmationTokenRepositoryPort emailConfirmationTokenRepositoryPort,
                               NotificationClient notificationClient,
                               ForgotPasswordUseCase forgotPasswordUseCase) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.emailConfirmationTokenRepositoryPort = emailConfirmationTokenRepositoryPort;
        this.notificationClient = notificationClient;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
    }

    @Override
    @Transactional
    public void execute(String email, String password, Role role) {

        if (userRepositoryPort.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }

        log.info("Criando Usuário...{}", email);

        User user = User.create(email, passwordEncoder.encode(password), role);

        if (role == Role.MERCHANT) {
            // MERCHANT cadastrado pelo ADMIN — email já confirmado automaticamente
            user.setEmailConfirmed(true);
            userRepositoryPort.save(user);
            forgotPasswordUseCase.execute(email);

        } else {
            userRepositoryPort.save(user);

            // ADMIN — fluxo normal de confirmação de email
            EmailConfirmationToken token = EmailConfirmationToken.create(user.getId());
            emailConfirmationTokenRepositoryPort.save(token);

            String confirmationLink = confirmationBaseUrl + "/api/auth/confirm?token=" + token.getToken();
            notificationClient.sendEmailConfirmationEvent(email, confirmationLink);
        }

    }
}
