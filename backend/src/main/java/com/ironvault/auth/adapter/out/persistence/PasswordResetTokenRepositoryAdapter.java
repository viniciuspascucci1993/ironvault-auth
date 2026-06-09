package com.ironvault.auth.adapter.out.persistence;

import com.ironvault.auth.adapter.out.mapper.PasswordResetTokenMapper;
import com.ironvault.auth.domain.model.PasswordResetToken;
import com.ironvault.auth.domain.port.out.PasswordResetTokenRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepositoryPort {

    private final PasswordResetTokenJpaRepository jpaRepository;
    private final PasswordResetTokenMapper mapper;

    public PasswordResetTokenRepositoryAdapter(PasswordResetTokenJpaRepository jpaRepository,
                                               PasswordResetTokenMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(token)));
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return jpaRepository.findByToken(token).map(mapper::toDomain);
    }
}
