package com.ironvault.auth.adapter.in.web;

import com.ironvault.auth.domain.port.in.GenerateApiKeyUseCase;
import com.ironvault.auth.domain.port.in.GetApiKeyUseCase;
import com.ironvault.auth.domain.port.in.RevokeApiKeyUseCase;
import com.ironvault.auth.domain.port.in.ValidateApiKeyUseCase;
import com.ironvault.auth.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/keys")
public class ApiKeyController {

    @Value("${app.internal.api-key}")
    private String internalApiKey;

    private final GenerateApiKeyUseCase generateApiKeyUseCase;
    private final GetApiKeyUseCase getApiKeyUseCase;
    private final ValidateApiKeyUseCase validateApiKeyUseCase;
    private final RevokeApiKeyUseCase revokeApiKeyUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    public ApiKeyController(GenerateApiKeyUseCase generateApiKeyUseCase,
                            GetApiKeyUseCase getApiKeyUseCase,
                            ValidateApiKeyUseCase validateApiKeyUseCase,
                            RevokeApiKeyUseCase revokeApiKeyUseCase,
                            JwtTokenProvider jwtTokenProvider) {
        this.generateApiKeyUseCase = generateApiKeyUseCase;
        this.getApiKeyUseCase = getApiKeyUseCase;
        this.validateApiKeyUseCase = validateApiKeyUseCase;
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

    public ResponseEntity<Map<String, Object>> validate(
            @RequestHeader("X-Internal-Key") String internalKey,
            @RequestParam("key") String apiKey
    ) {
        if (!internalApiKey.equals(internalKey)) {
            return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Unauthorized"));
        }

        boolean valid = validateApiKeyUseCase.execute(apiKey);
        return ResponseEntity.ok(Map.of("valid", valid));
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
