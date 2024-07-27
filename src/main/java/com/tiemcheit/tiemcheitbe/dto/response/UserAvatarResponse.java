package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserAvatarResponse {
    private Long id;
    private String image;
    private String username;
}
