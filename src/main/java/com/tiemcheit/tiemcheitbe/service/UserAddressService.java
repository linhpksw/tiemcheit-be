package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.UserAddressDto;
import com.tiemcheit.tiemcheitbe.mapper.UserAddressMapper;
import com.tiemcheit.tiemcheitbe.repository.UserAddressRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserAddressRepo userAddressRepo;

    private final UserAddressMapper userAddressMapper;

    public List<UserAddressDto> getAddressByUserId(Long id) {
        return userAddressMapper.toDtos(userAddressRepo.findByUserId(id));
    }
}
