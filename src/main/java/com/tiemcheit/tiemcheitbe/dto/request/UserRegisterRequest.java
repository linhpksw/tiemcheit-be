package com.tiemcheit.tiemcheitbe.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    private String username;

    @NotBlank(message = "FULLNAME_REQUIRED")
    @Size(max = 100, message = "FULLNAME_TOO_LONG")
    private String fullname;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, message = "PASSWORD_INVALID")
    private String password;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_INVALID")
    private String email;

    @NotBlank(message = "PHONE_REQUIRED")
    @Pattern(regexp = "(84|0[35789])([0-9]{8})", message = "PHONE_INVALID")
    private String phone;
}
