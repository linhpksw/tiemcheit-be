package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.UserAddressRequest;
import com.tiemcheit.tiemcheitbe.dto.response.UserAddressResponse;
import com.tiemcheit.tiemcheitbe.model.UserAddress;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {

    UserAddress toEntity(UserAddressRequest request);

    UserAddressResponse toResponse(UserAddress address);

    List<UserAddressResponse> toResponses(List<UserAddress> userAddresses);

}
