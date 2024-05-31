package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

@Data
public class AddressResponse {
    private Long id;
    private String address;
    private Boolean isDefault;
}
