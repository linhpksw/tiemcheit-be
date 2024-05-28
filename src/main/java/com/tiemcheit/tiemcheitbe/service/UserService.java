package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.model.User;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public User getById(Long id) {
        return userRepo.getReferenceById(id);
    }
}
