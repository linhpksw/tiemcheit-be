package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CustomerRequest;
import com.tiemcheit.tiemcheitbe.dto.response.UserProfileResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.RoleMapper;
import com.tiemcheit.tiemcheitbe.mapper.UserMapper;
import com.tiemcheit.tiemcheitbe.model.Role;
import com.tiemcheit.tiemcheitbe.repository.OrderRepo;
import com.tiemcheit.tiemcheitbe.repository.RoleRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final OrderRepo orderRepo;
    private final UserMapper userMapper;
    private final UserService userService;
    private final RoleMapper roleMapper;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserProfileResponse> allCustomer() {
        List<UserProfileResponse> users = userService.getUsersProfile();
        List<UserProfileResponse> customers = new ArrayList<>();
        Role role = roleRepo.findByName("CUSTOMER").orElseThrow(() -> new AppException("Role not found.", HttpStatus.NOT_FOUND));
        for (UserProfileResponse u : users) {
            if (u.getRoles().contains(roleMapper.toRoleResponse(role))) {
                u.setOrderNumber(orderRepo.countByUser_Id(u.getId()));
                u.setOrderTotal(orderRepo.getTotalAmountSpentByUser(u.getId()));
                customers.add(u);
            }
        }

        return customers;
    }

    public UserProfileResponse getCustomerById(Long id) {
        return userMapper.toUserProfileResponse(userRepo.getReferenceById(id));
    }

    public UserProfileResponse updateCustomer(CustomerRequest customerRequest) {
        return userService.updateUserProfile(customerRequest.getUsername(), customerRequest.getUpdateData());
    }
}
