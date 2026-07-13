package com.ironvault.auth.domain.port.in;

import com.ironvault.auth.adapter.out.entity.LoginLogEntity;

import java.util.List;

public interface GetAllLoginLogsUseCase {
    List<LoginLogEntity> execute();
}
