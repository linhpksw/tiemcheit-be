package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.UserRegisterRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartUserResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserDetailResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserResponse;
import com.tiemcheit.tiemcheitbe.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(UserRegisterRequest request);

    UserResponse toUserResponse(User user);

    CartUserResponse toCartUserResponse(User user);

    UserDetailResponse toUserDetailResponse(User user);
}
