package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgotPasswordRequest {
    private String email;
}
