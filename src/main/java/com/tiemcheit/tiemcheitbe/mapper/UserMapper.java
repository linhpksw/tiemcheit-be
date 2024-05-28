package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.response.CartUserResponse;
import com.tiemcheit.tiemcheitbe.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    CartUserResponse toCartUserResponse(User user);

    User toEntity(CartUserResponse userDto);
}
