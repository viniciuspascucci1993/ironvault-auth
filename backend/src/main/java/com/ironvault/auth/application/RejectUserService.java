package com.ironvault.auth.application;

import com.ironvault.auth.adapter.out.client.NotificationClient;
import com.ironvault.auth.domain.enums.ApprovalStatus;
import com.ironvault.auth.domain.enums.Role;
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
    private final NotificationClient notificationClient;

    public RejectUserService(UserRepositoryPort userRepositoryPort, NotificationClient notificationClient) {
        this.userRepositoryPort = userRepositoryPort;
        this.notificationClient = notificationClient;
    }

    @Override
    @Transactional
    public void execute(UUID userId) {
        var user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        if (user.getRole() != Role.MERCHANT) {
            throw new IllegalArgumentException("Only MERCHANT users can be rejected");
        }

        if (user.getApprovalStatus() == ApprovalStatus.REJECTED) {
            throw new IllegalArgumentException("User is already rejected");
        }

        user.setApprovalStatus(ApprovalStatus.REJECTED);
        user.setActive(false);
        userRepositoryPort.save(user);

        notificationClient.sendMerchantRejectedEvent(user.getEmail());

        log.info("User rejected userId={}", userId);

    }
}
