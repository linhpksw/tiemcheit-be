package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserAddAddressRequest {
    private String address;
    private Boolean isDefault;
}
