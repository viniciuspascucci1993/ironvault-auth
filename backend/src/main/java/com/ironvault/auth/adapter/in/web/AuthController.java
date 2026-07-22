package com.ironvault.auth.adapter.in.web;

import com.ironvault.auth.adapter.in.web.request.*;
import com.ironvault.auth.adapter.in.web.response.AuthResponse;
import com.ironvault.auth.domain.port.in.*;
import com.ironvault.auth.utils.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    private final ConfirmationEmailUseCase confirmationEmailUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUseCase loginUseCase,
                          ConfirmationEmailUseCase confirmationEmailUseCase,
                          RefreshTokenUseCase refreshTokenUseCase,
                          ForgotPasswordUseCase forgotPasswordUseCase,
                          ResetPasswordUseCase resetPasswordUseCase,
                          ChangePasswordUseCase changePasswordUseCase,
                          JwtTokenProvider jwtTokenProvider) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
        this.confirmationEmailUseCase = confirmationEmailUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {

        registerUserUseCase.execute(request.getEmail(), request.getPassword(), request.getRole(), request.getSource());
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

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        forgotPasswordUseCase.execute(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.execute(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/reset-password", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> resetPasswordForm(@RequestParam String token) {
        return ResponseEntity.ok("""
            <html>
            <body style="font-family: Arial; text-align: center; padding: 50px;">
                <h1 style="color: #1a1a2e;">🔑 Redefinir senha</h1>
                <form method="POST" action="/api/auth/reset-password-form">
                    <input type="hidden" name="token" value="%s"/>
                    <input type="password" name="newPassword" placeholder="Nova senha"
                           style="padding: 10px; margin: 10px; border-radius: 5px; border: 1px solid #ccc;"/><br/>
                    <button type="submit"
                            style="background: #4f46e5; color: white; padding: 12px 30px; border: none; border-radius: 8px; cursor: pointer;">
                        Redefinir senha
                    </button>
                </form>
            </body>
            </html>
            """.formatted(token));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtTokenProvider.extractUserId(token);
        changePasswordUseCase.execute(userId, request.getCurrentPassword(), request.getNewPassword());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password-form")
    public ResponseEntity<String> processResetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        resetPasswordUseCase.execute(token, newPassword);
        return ResponseEntity.ok("""
            <html>
            <body style="font-family: Arial; text-align: center; padding: 50px;">
                <h1 style="color: #28a745;">✅ Senha redefinida com sucesso!</h1>
                <p style="color: #555;">Você já pode fazer login com sua nova senha.</p>
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
