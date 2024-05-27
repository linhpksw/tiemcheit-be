package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.UserDto;
import com.tiemcheit.tiemcheitbe.mapper.UserMapper;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    private final UserMapper userMapper;

    public UserDto getUser(Long id) {
        return userMapper.toDto(userRepo.findById(id).get());
    }
}
