package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.response.UserAvatarResponse;
import com.tiemcheit.tiemcheitbe.model.UserAvatar;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAvatarMapper {
    UserAvatarResponse toUserAvatarResponse(UserAvatar userAvatar);
}
