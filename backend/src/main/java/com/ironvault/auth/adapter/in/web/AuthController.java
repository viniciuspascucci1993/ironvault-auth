package com.ironvault.auth.adapter.in.web;

import com.ironvault.auth.adapter.in.web.request.LoginRequest;
import com.ironvault.auth.adapter.in.web.request.RegisterRequest;
import com.ironvault.auth.adapter.in.web.response.AuthResponse;
import com.ironvault.auth.domain.port.in.ConfirmationEmailUseCase;
import com.ironvault.auth.domain.port.in.LoginUseCase;
import com.ironvault.auth.domain.port.in.RefreshTokenUseCase;
import com.ironvault.auth.domain.port.in.RegisterUserUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    private final ConfirmationEmailUseCase confirmationEmailUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUseCase loginUseCase,
                          ConfirmationEmailUseCase confirmationEmailUseCase,
                          RefreshTokenUseCase refreshTokenUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
        this.confirmationEmailUseCase = confirmationEmailUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {

        registerUserUseCase.execute(request.getEmail(), request.getPassword(), request.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        AuthResponse token = loginUseCase.execute(request.getEmail(), request.getPassword(), ip, userAgent);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        return ResponseEntity.ok(refreshTokenUseCase.refresh(refreshToken));
    }

    @GetMapping(value = "/confirm", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> confirm(@RequestParam String token) {
        confirmationEmailUseCase.confirm(token);
        return ResponseEntity.ok("""
                <html>
                <body style="font-family: Arial; text-align: center; padding: 50px;">
                    <h1 style="color: #28a745;">✅ Email confirmado com sucesso!</h1>
                    <p style="color: #555;">Você já pode fazer login na plataforma.</p>
                </body>
                </html>
                """);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
