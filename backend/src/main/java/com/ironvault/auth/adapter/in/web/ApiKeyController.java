package com.ironvault.auth.adapter.in.web;

import com.ironvault.auth.domain.port.in.GenerateApiKeyUseCase;
import com.ironvault.auth.domain.port.in.GetApiKeyUseCase;
import com.ironvault.auth.domain.port.in.RevokeApiKeyUseCase;
import com.ironvault.auth.utils.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/keys")
public class ApiKeyController {

    private final GenerateApiKeyUseCase generateApiKeyUseCase;
    private final GetApiKeyUseCase getApiKeyUseCase;
    private final RevokeApiKeyUseCase revokeApiKeyUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    public ApiKeyController(GenerateApiKeyUseCase generateApiKeyUseCase,
                            GetApiKeyUseCase getApiKeyUseCase,
                            RevokeApiKeyUseCase revokeApiKeyUseCase,
                            JwtTokenProvider jwtTokenProvider) {
        this.generateApiKeyUseCase = generateApiKeyUseCase;
        this.getApiKeyUseCase = getApiKeyUseCase;
        this.revokeApiKeyUseCase = revokeApiKeyUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generate(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtTokenProvider.extractUserId(token);
        String key = generateApiKeyUseCase.execute(userId);
        return ResponseEntity.ok(Map.of("apiKey", key));
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> get(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtTokenProvider.extractUserId(token);
        return getApiKeyUseCase.execute(userId)
                .map(key -> ResponseEntity.ok(Map.of("apiKey", key)))
                .orElse(ResponseEntity.ok(Map.of("apiKey", "")));
    }

    @DeleteMapping("/revoke")
    public ResponseEntity<Void> revoke(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtTokenProvider.extractUserId(token);
        revokeApiKeyUseCase.execute(userId);
        return ResponseEntity.ok().build();
    }
}
