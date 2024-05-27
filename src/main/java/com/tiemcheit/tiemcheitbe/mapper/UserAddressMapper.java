package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.UserAddressDto;
import com.tiemcheit.tiemcheitbe.model.UserAddress;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserAddressMapper {

    UserAddress toEntity(UserAddress addressDto);

    UserAddressDto toDto(UserAddress address);

    List<UserAddressDto> toDtos(List<UserAddress> userAddresses);

}
