package com.ironvault.auth.application;

import com.ironvault.auth.adapter.out.client.NotificationClient;
import com.ironvault.auth.domain.enums.ApprovalStatus;
import com.ironvault.auth.domain.enums.Role;
import com.ironvault.auth.domain.exception.UserNotFoundException;
import com.ironvault.auth.domain.port.in.ApproveUserUseCase;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class ApproveUserService implements ApproveUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final NotificationClient notificationClient;

    public ApproveUserService(UserRepositoryPort userRepositoryPort, NotificationClient notificationClient) {
        this.userRepositoryPort = userRepositoryPort;
        this.notificationClient = notificationClient;
    }

    @Override
    @Transactional
    public void execute(UUID userId) {
        var user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        if (user.getRole() != Role.MERCHANT) {
            throw new IllegalArgumentException("Only MERCHANT users can be approved");
        }

        if (user.getApprovalStatus() == ApprovalStatus.APPROVED) {
            throw new IllegalArgumentException("User is already approved");
        }

        user.setApprovalStatus(ApprovalStatus.APPROVED);
        user.setActive(true);
        userRepositoryPort.save(user);

        notificationClient.sendMerchantApprovedEvent(user.getEmail());

        log.info("User approved userId={}", userId);
    }
}
