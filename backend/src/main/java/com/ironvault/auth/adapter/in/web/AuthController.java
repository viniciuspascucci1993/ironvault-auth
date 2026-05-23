package com.ironvault.auth.adapter.in.web;

import com.ironvault.auth.adapter.in.web.request.LoginRequest;
import com.ironvault.auth.adapter.in.web.request.RegisterRequest;
import com.ironvault.auth.adapter.in.web.response.AuthResponse;
import com.ironvault.auth.domain.port.in.LoginUseCase;
import com.ironvault.auth.domain.port.in.RegisterUserUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUseCase loginUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {

        registerUserUseCase.execute(request.getEmail(), request.getPassword(), request.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletRequest httpRequest) {

        String ip = getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        AuthResponse token = loginUseCase.execute(request.getEmail(), request.getPassword(),
                ip, userAgent);
        return ResponseEntity.ok(token);
    }

    private String getClientIp(HttpServletRequest httpRequest) {

        String xFowardFor = httpRequest.getHeader("X-Forwarded-For");
        if (xFowardFor != null && !xFowardFor.isEmpty()) {
            return xFowardFor.split(",")[0].trim();
        }

        return httpRequest.getRemoteAddr();
    }
}
