package com.ironvault.auth.application;

import com.ironvault.auth.domain.exception.UserNotFoundException;
import com.ironvault.auth.domain.port.in.UpdateUserMerchantIdUseCase;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UpdateUserMerchantIdService implements UpdateUserMerchantIdUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UpdateUserMerchantIdService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public void execute(UUID userId, UUID merchantId) {
        var user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));
        user.setMerchantId(merchantId);
        userRepositoryPort.save(user);
        log.info("User merchantId updated userId={} merchantId={}", userId, merchantId);
    }
}
