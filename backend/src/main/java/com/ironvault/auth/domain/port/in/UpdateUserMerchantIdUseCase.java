package com.ironvault.auth.domain.port.in;

import java.util.UUID;

public interface UpdateUserMerchantIdUseCase {
    void execute(UUID userId, UUID merchantId);
}
