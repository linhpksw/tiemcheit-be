package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserInfoResponse {
    private String username;
    private String fullname;
    private Set<RoleResponse> roles;

}
