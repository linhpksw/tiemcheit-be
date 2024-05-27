package com.tiemcheit.tiemcheitbe.dto;

import lombok.Data;

@Data
public class UserAddressDto {
    private Long id;
    private String address;
    private Boolean isDefault;

}
