package com.ironvault.auth.adapter.in.web;

import com.ironvault.auth.adapter.in.web.response.LoginLogResponse;
import com.ironvault.auth.adapter.out.entity.LoginLogEntity;
import com.ironvault.auth.domain.port.in.GetAllLoginLogsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth/login-logs")
public class LoginLogsController {

    private final GetAllLoginLogsUseCase getAllLoginLogsUseCase;

    public LoginLogsController(GetAllLoginLogsUseCase getAllLoginLogsUseCase) {
        this.getAllLoginLogsUseCase = getAllLoginLogsUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoginLogResponse>> listAll() {
        return ResponseEntity.ok(getAllLoginLogsUseCase.execute()
                .stream()
                .map(LoginLogResponse::of)
                .toList());
    }
}
