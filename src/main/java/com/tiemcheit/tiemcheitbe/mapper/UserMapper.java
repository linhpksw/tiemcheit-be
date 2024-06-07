package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.UserRegisterRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CustomerResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserDetailResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserResponse;
import com.tiemcheit.tiemcheitbe.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(UserRegisterRequest request);

    UserResponse toUserResponse(User user);

    UserDetailResponse toUserDetailResponse(User user);

    List<CustomerResponse> toCustomerResponses(List<User> user);
}
