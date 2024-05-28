package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.UserRequest;
import com.tiemcheit.tiemcheitbe.dto.response.UserResponse;
import com.tiemcheit.tiemcheitbe.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserResponse toResponse(User user);

    User toEntity(UserRequest request);
}
