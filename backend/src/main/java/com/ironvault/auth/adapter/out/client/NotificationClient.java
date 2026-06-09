package com.ironvault.auth.adapter.out.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@Slf4j
public class NotificationClient {

    private final WebClient webClient;
    private final String apiKey;

    public NotificationClient(@Value("${app.notifications.url}") String notificationsUrl,
                              @Value("${app.notifications.api-key}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(notificationsUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void sendUserRegisteredEvent(String email, String name) {
        try {
            webClient.post()
                    .uri("/api/notifications/events")
                    .header("X-API-KEY", apiKey)
                    .bodyValue(Map.of(
                            "type", "USER_REGISTERED",
                            "sourceService", "ironvault-auth",
                            "payload", Map.of(
                                    "email", email,
                                    "name", name
                            )
                    ))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe(
                            null,
                            error -> log.error("Failed to send USER_REGISTERED event email={} reason={}", email, error.getMessage())
                    );
        } catch (Exception ex) {
            log.error("Failed to send notification email={} reason={}", email, ex.getMessage());
        }
    }

    public void sendEmailConfirmationEvent(String email, String confirmationLink) {
        try {
            webClient.post()
                    .uri("/api/notifications/events")
                    .header("X-API-KEY", apiKey)
                    .bodyValue(Map.of(
                            "type", "EMAIL_CONFIRMATION",
                            "sourceService", "ironvault-auth",
                            "payload", Map.of(
                                    "email", email,
                                    "confirmationLink", confirmationLink
                            )
                    ))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe(
                            null,
                            error -> log.error("Failed to send EMAIL_CONFIRMATION event email={} reason={}",
                                    email, error.getMessage())
                    );


        } catch (Exception ex) {
            log.error("Failed to send notification email={} reason={}", email, ex.getMessage());
        }
    }

    public void sendPasswordResetEvent(String email, String resetLink) {
        try {
            webClient.post()
                    .uri("/api/notifications/events")
                    .header("X-API-KEY", apiKey)
                    .bodyValue(Map.of(
                            "type", "PASSWORD_RESET",
                            "sourceService", "ironvault-auth",
                            "payload", Map.of(
                                    "email", email,
                                    "resetLink", resetLink
                            )
                    ))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe(
                            null,
                            error -> log.error("Failed to send PASSWORD_RESET event email={} reason={}", email, error.getMessage())
                    );
        } catch (Exception ex) {
            log.error("Failed to send notification email={} reason={}", email, ex.getMessage());
        }
    }
}
