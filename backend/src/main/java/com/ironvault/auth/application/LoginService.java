package com.ironvault.auth.application;

import com.ironvault.auth.adapter.in.web.response.AuthResponse;
import com.ironvault.auth.domain.exception.EmailNotConfirmedException;
import com.ironvault.auth.domain.exception.InvalidCredentialsException;
import com.ironvault.auth.domain.exception.TooManyRequestsException;
import com.ironvault.auth.domain.exception.UserNotFoundException;
import com.ironvault.auth.domain.model.RefreshToken;
import com.ironvault.auth.domain.model.User;
import com.ironvault.auth.domain.port.in.LoginUseCase;
import com.ironvault.auth.domain.port.out.RefreshTokenRepositoryPort;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import com.ironvault.auth.utils.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class LoginService implements LoginUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final RateLimitingService rateLimitingService;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    public LoginService(UserRepositoryPort userRepositoryPort,
                PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
                RefreshTokenRepositoryPort refreshTokenRepositoryPort,
                RateLimitingService rateLimitingService) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepositoryPort = refreshTokenRepositoryPort;
        this.rateLimitingService = rateLimitingService;
    }

    @Override
    @Transactional
    public AuthResponse execute(String email, String password, String ip, String userAgent) {
        log.info("Logando com...E-mail: {}", email);

        // Rate Limiting por IP ou por E-mail
        if (!rateLimitingService.isAllowed("login:ip:" + ip) ||
        !rateLimitingService.isAllowed("login:email:" + email)) {
            throw new TooManyRequestsException("Muitas tentativas. Tente novamente em 15 minutos.");
        }

        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        // Validar se email esta confirmado
        if (!user.isEmailConfirmed()) {
            throw new EmailNotConfirmedException("Por favor, confirme seu email antes de fazer login.");
        }

        rateLimitingService.reset("login:ip:" + ip);
        rateLimitingService.reset("login:email:" + email);

        refreshTokenRepositoryPort.revokeAllByUserId(user.getId());

        String token = jwtTokenProvider.generateToken(user, ip, userAgent);

        String rawRefreshToken = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.create(user.getId(), rawRefreshToken, refreshExpirationMs);
        refreshTokenRepositoryPort.save(refreshToken);

        log.info("Login successful userId={} ip={}", user.getId(), ip);

        return AuthResponse.of(
                token,
                jwtTokenProvider.getExpirationMs(),
                rawRefreshToken,
                user.getEmail(),
                user.getRole().name()
        );
    }
}
