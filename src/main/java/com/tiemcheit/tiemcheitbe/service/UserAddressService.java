package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.UserAddressRequest;
import com.tiemcheit.tiemcheitbe.dto.response.UserAddressResponse;
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

    private final UserService userService;

    public List<UserAddressResponse> getAddressByUserId(Long id) {
        return userAddressMapper.toResponses(userAddressRepo.findByUserId(id));
    }

    public UserAddressResponse addUserAddress(UserAddressRequest request, Long userId) {
        request.getUser().setId(userId);
        request.setIsDefault(false);
        return userAddressMapper.toResponse(userAddressRepo.save(userAddressMapper.toEntity(request)));
    }

}
