package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserAddAddressResponse {
    private Long id;
    private String address;
    private Boolean isDefault;
}
