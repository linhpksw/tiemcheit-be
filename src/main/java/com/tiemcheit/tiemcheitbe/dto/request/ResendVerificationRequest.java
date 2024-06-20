package com.tiemcheit.tiemcheitbe.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResendVerificationRequest {
    @Email(message = "EMAIL_INVALID")
    private String email;

    private String type;
}
