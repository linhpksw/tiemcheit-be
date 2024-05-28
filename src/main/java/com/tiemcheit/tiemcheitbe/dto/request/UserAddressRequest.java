package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Data;

@Data
public class UserAddressRequest {
    private UserRequest user;
    private String address;
    private Boolean isDefault;
}
