package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CustomerRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CustomerResponse;
import com.tiemcheit.tiemcheitbe.mapper.UserMapper;
import com.tiemcheit.tiemcheitbe.model.Role;
import com.tiemcheit.tiemcheitbe.model.User;
import com.tiemcheit.tiemcheitbe.repository.RoleRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final UserMapper userMapper;

    public List<CustomerResponse> allCustomer() {
        List<User> customers = userRepo.findUsersByRole("CUSTOMER");
        List<CustomerResponse> customerResponses = new ArrayList<>();
        for (User u : customers) {
            customerResponses.add(userMapper.toCustomerResponse(u));
        }
        return customerResponses;
    }

    public CustomerResponse getCustomerById(Long id) {
        return userMapper.toCustomerResponse(userRepo.getReferenceById(id));
    }

    public CustomerResponse updateCustomer(CustomerRequest customerRequest) {
        User updatedUser = userRepo.getReferenceById(customerRequest.getId());

        if (!customerRequest.getStatus().isEmpty()) {
            updatedUser.setStatus(customerRequest.getStatus());
        }

        if (customerRequest.getRole() != 0) {
            Set<Role> newRoles = new HashSet<>();
            newRoles.add(roleRepo.getReferenceById(customerRequest.getRole()));
            updatedUser.setRoles(newRoles);
        }

        userRepo.save(updatedUser);
        return userMapper.toCustomerResponse(updatedUser);
    }
}
