package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeactivateRequest {
    private String username;
    private String currentPassword;
    private String token;
}
