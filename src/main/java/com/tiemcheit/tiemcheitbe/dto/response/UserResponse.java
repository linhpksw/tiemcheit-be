package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String phone;
    private String email;
}
