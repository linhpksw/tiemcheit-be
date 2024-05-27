package com.tiemcheit.tiemcheitbe.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String phone;
    private String email;
}
