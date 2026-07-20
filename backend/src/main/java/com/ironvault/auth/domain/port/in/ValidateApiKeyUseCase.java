package com.ironvault.auth.domain.port.in;

public interface ValidateApiKeyUseCase {
    boolean execute(String apiKey);
}
