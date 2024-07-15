package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Data;

@Data
public class EmployeeRequest {
    private String username;
    private UserUpdateRequest updateData;
}
