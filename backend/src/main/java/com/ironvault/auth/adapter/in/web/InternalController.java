package com.ironvault.auth.adapter.in.web;

import com.ironvault.auth.domain.port.in.UpdateUserMerchantIdUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/internal")
public class InternalController {

    @Value("${app.internal.api-key}")
    private String internalApiKey;

    private final UpdateUserMerchantIdUseCase updateUserMerchantIdUseCase;

    public InternalController(UpdateUserMerchantIdUseCase updateUserMerchantIdUseCase) {
        this.updateUserMerchantIdUseCase = updateUserMerchantIdUseCase;
    }

    @PostMapping("/users/{userId}/merchant")
    public ResponseEntity<Void> updateMerchantId(
            @RequestHeader("X-Internal-Key") String internalKey,
            @PathVariable UUID userId,
            @RequestBody Map<String, String> body) {

        if (!internalApiKey.equals(internalKey)) {
            return ResponseEntity.status(401).build();
        }

        UUID merchantId = UUID.fromString(body.get("merchantId"));
        updateUserMerchantIdUseCase.execute(userId, merchantId);

        return ResponseEntity.ok().build();
    }
}
