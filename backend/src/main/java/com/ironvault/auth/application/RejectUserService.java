package com.ironvault.auth.application;

import com.ironvault.auth.domain.enums.ApprovalStatus;
import com.ironvault.auth.domain.exception.UserNotFoundException;
import com.ironvault.auth.domain.port.in.RejectUserUseCase;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class RejectUserService implements RejectUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public RejectUserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    @Transactional
    public void execute(UUID userId) {
        var user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        user.setApprovalStatus(ApprovalStatus.REJECTED);
        user.setActive(false);
        userRepositoryPort.save(user);

        log.info("User rejected userId={}", userId);

    }
}
