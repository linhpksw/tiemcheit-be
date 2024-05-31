package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    private String id;
    private String username;
    private String fullname;
    private String email;
    private String phone;
    private Date dob;
    private String status;
    private boolean isActivated;
    private Date createdAt;
    private Date updatedAt;
    private Set<AddressResponse> addresses;
}
