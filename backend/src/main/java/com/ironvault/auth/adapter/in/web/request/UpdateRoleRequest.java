package com.ironvault.auth.adapter.in.web.request;

import com.ironvault.auth.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleRequest {
    private Role role;
}
