package com.ironvault.auth.adapter.out.persistence;

import com.ironvault.auth.adapter.out.entity.RefreshTokenEntity;
import com.ironvault.auth.adapter.out.mapper.RefreshTokenMapper;
import com.ironvault.auth.domain.model.RefreshToken;
import com.ironvault.auth.domain.port.out.RefreshTokenRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {

    private final RefreshTokenJpaRepository jpaRepository;
    private final RefreshTokenMapper refreshTokenMapper;

    public RefreshTokenRepositoryAdapter(RefreshTokenJpaRepository jpaRepository,
                                         RefreshTokenMapper refreshTokenMapper) {
        this.jpaRepository = jpaRepository;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = refreshTokenMapper.toEntity(refreshToken);
        return refreshTokenMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepository.findByToken(token).map(refreshTokenMapper::toDomain);
    }

    @Override
    @Transactional
    public void revokeAllByUserId(UUID userId) {
        jpaRepository.revokeAllByUserId(userId);
    }
}
