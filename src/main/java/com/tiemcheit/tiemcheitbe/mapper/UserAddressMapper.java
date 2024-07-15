package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.response.UserAddAddressResponse;
import com.tiemcheit.tiemcheitbe.model.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {
    UserAddressMapper INSTANCE = Mappers.getMapper(UserAddressMapper.class);

    UserAddAddressResponse toUserAddAddressResponse(UserAddress userAddress);
}
