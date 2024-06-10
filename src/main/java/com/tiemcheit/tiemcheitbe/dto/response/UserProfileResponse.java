package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class UserProfileResponse {
    private String username;
    private String fullname;
    private String email;
    private String phone;
    private Date dob;
    private String gender;
    private String status;
    private boolean isActivated;
    private Date createdAt;
    private Date updatedAt;
    private Set<AddressResponse> addresses;
    private Set<RoleResponse> roles;
}
