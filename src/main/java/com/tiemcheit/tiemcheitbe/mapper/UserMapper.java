package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.UserDto;
import com.tiemcheit.tiemcheitbe.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
