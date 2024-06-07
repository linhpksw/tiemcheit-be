package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.response.CustomerResponse;
import com.tiemcheit.tiemcheitbe.mapper.UserMapper;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public List<CustomerResponse> allCustomer() {
        return userMapper.toCustomerResponses(userRepo.findUsersByRole("CUSTOMER"));
    }

}
