package com.tiemcheit.tiemcheitbe.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyRequest {

    @Size(min = 6, max = 6, message = "CODE_INVALID")
    private String code;

    @Email(message = "EMAIL_INVALID")
    private String email;

    private String type;
}
