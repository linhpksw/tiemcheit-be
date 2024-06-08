package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Data;

@Data
public class CustomerRequest {
    private Long id;
    private String status;
    private Long role;
}
