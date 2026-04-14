package com.ironvault.auth.application;

import com.ironvault.auth.domain.exception.InvalidCredentialsException;
import com.ironvault.auth.domain.exception.UserNotFoundException;
import com.ironvault.auth.domain.model.User;
import com.ironvault.auth.domain.port.in.LoginUseCase;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import com.ironvault.auth.utils.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginService implements LoginUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(UserRepositoryPort userRepositoryPort,
                PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String execute(String email, String password) {

        log.info("Logando com...E-mail: {}", email);

        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return jwtTokenProvider.generateToken(user);
    }
}
