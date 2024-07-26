package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAvatarRepo extends JpaRepository<UserAvatar, Long> {
    UserAvatar findByUser_Username(String username);
}
