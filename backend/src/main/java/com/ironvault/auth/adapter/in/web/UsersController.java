package com.ironvault.auth.adapter.in.web;

import com.ironvault.auth.adapter.in.web.request.UpdateRoleRequest;
import com.ironvault.auth.adapter.in.web.request.UpdateStatusRequest;
import com.ironvault.auth.adapter.in.web.response.UserResponse;
import com.ironvault.auth.domain.model.User;
import com.ironvault.auth.domain.port.in.GetAllUsersUseCase;
import com.ironvault.auth.domain.port.in.GetUserByIdUseCase;
import com.ironvault.auth.domain.port.in.UpdateUserRoleUseCase;
import com.ironvault.auth.domain.port.in.UpdateUserStatusUseCase;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserStatusUseCase updateUserStatusUseCase;
    private final UpdateUserRoleUseCase updateUserRoleUseCase;

    public UsersController(GetAllUsersUseCase getAllUsersUseCase,
                           GetUserByIdUseCase getUserByIdUseCase,
                           UpdateUserStatusUseCase updateUserStatusUseCase,
                           UpdateUserRoleUseCase updateUserRoleUseCase) {
        this.getAllUsersUseCase = getAllUsersUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.updateUserStatusUseCase = updateUserStatusUseCase;
        this.updateUserRoleUseCase = updateUserRoleUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> listAll() {
        return ResponseEntity.ok(getAllUsersUseCase.execute()
                .stream()
                .map(UserResponse::of)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(UserResponse.of(getUserByIdUseCase.execute(id)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateStatus(@PathVariable UUID id,
                                             @RequestBody UpdateStatusRequest request) {
        updateUserStatusUseCase.execute(id, request.isActive());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRole(@PathVariable UUID id,
                                           @RequestBody UpdateRoleRequest request) {
        updateUserRoleUseCase.execute(id, request.getRole());
        return ResponseEntity.ok().build();
    }
}
