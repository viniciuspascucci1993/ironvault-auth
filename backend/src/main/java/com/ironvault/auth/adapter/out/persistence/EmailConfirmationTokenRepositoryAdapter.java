package com.ironvault.auth.adapter.out.persistence;

import com.ironvault.auth.adapter.out.mapper.EmailConfirmationTokenMapper;
import com.ironvault.auth.domain.model.EmailConfirmationToken;
import com.ironvault.auth.domain.port.out.EmailConfirmationTokenRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmailConfirmationTokenRepositoryAdapter implements EmailConfirmationTokenRepositoryPort {

    private final EmailConfirmationTokenJpaRepository jpaRepository;
    private final EmailConfirmationTokenMapper mapper;

    public EmailConfirmationTokenRepositoryAdapter(EmailConfirmationTokenJpaRepository jpaRepository,
                   EmailConfirmationTokenMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public EmailConfirmationToken save(EmailConfirmationToken token) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(token)));
    }

    @Override
    public Optional<EmailConfirmationToken> findByToken(String token) {
        return jpaRepository.findByToken(token).map(mapper::toDomain);
    }
}
