package com.ironvault.auth.application;

import com.ironvault.auth.adapter.out.entity.LoginLogEntity;
import com.ironvault.auth.adapter.out.persistence.LoginLogJpaRepository;
import com.ironvault.auth.domain.port.in.GetAllLoginLogsUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GetAllLoginLogsService implements GetAllLoginLogsUseCase {

    private final LoginLogJpaRepository loginLogJpaRepository;

    public GetAllLoginLogsService(LoginLogJpaRepository loginLogJpaRepository) {
        this.loginLogJpaRepository = loginLogJpaRepository;
    }

    @Override
    public List<LoginLogEntity> execute() {
        log.info("Fetching all login logs");
        return loginLogJpaRepository.findAll();
    }
}
