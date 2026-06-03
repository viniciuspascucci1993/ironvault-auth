package com.ironvault.auth.domain.port.out;

import com.ironvault.auth.domain.model.EmailConfirmationToken;

import java.util.Optional;

public interface EmailConfirmationTokenRepositoryPort {
    EmailConfirmationToken save(EmailConfirmationToken token);
    Optional<EmailConfirmationToken> findByToken(String token);
}
