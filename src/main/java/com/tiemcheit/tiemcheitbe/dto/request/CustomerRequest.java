package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Data;

@Data
public class CustomerRequest {
    private String username;
    private UserUpdateRequest updateData;
}
