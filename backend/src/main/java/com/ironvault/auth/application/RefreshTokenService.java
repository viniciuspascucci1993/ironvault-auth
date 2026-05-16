package com.ironvault.auth.application;

import com.ironvault.auth.domain.port.in.RefreshTokenUseCase;
import com.ironvault.auth.utils.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService implements RefreshTokenUseCase {

    private final JwtTokenProvider jwtTokenProvider;

    public RefreshTokenService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String execute(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        String email = jwtTokenProvider.extractEmail(refreshToken);
        return jwtTokenProvider.generateTokenFromEmail(email);
    }
}
