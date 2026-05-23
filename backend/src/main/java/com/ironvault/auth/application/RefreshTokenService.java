package com.ironvault.auth.application;

import com.ironvault.auth.adapter.in.web.response.AuthResponse;
import com.ironvault.auth.domain.exception.InvalidCredentialsException;
import com.ironvault.auth.domain.port.in.RefreshTokenUseCase;
import com.ironvault.auth.domain.port.out.RefreshTokenRepositoryPort;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import com.ironvault.auth.utils.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final JwtTokenProvider jwtTokenProvider;

    public RefreshTokenService(RefreshTokenRepositoryPort refreshTokenRepositoryPort,
                               UserRepositoryPort userRepositoryPort,
                               JwtTokenProvider jwtTokenProvider) {
        this.refreshTokenRepositoryPort = refreshTokenRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponse refresh(String refreshToken) {

        var token = refreshTokenRepositoryPort.findByToken(refreshToken)
                        .orElseThrow(() -> {
                            log.warn("Refresh Token Not Found");
                            return new InvalidCredentialsException();
                        });

        if (token.isRevoked() || token.isExpired()) {
            log.warn("Refresh token revoked or expired userId={}", token.getUserId());
            throw new InvalidCredentialsException();
        }

        var user = userRepositoryPort.findById(token.getUserId())
                        .orElseThrow(InvalidCredentialsException::new);

        String newAccessToken = jwtTokenProvider.generateToken(user, null, null);

        log.info("Token refreshed for userId={}", user.getId());

        return AuthResponse.of(
                newAccessToken,
                jwtTokenProvider.getExpirationMs(),
                refreshToken,
                user.getEmail(),
                user.getRole().name()
        );
    }
}
