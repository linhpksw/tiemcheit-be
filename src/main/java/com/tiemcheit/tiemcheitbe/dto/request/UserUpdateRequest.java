package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
public class UserUpdateRequest {
    private String password;
    private String fullname;
    private String phone;
    private String email;
    private String gender;
    private Date dob;
    private String status;
    private Boolean isActivated;
    private Set<RoleRequest> roles;
    private Set<AddressRequest> addresses;
}
