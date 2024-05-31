package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String password;
    private String fullname;
    private Date dob;
    private Set<String> roles;
    private Set<AddressRequest> addresses;
}
