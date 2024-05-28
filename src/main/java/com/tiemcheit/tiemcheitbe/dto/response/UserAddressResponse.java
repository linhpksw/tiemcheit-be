package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

@Data
public class UserAddressResponse {
    private String address;
    private Boolean isDefault;
}
