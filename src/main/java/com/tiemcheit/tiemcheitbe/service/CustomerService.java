package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CustomerRequest;
import com.tiemcheit.tiemcheitbe.dto.response.UserProfileResponse;
import com.tiemcheit.tiemcheitbe.repository.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.RoleMapper;
import com.tiemcheit.tiemcheitbe.mapper.UserMapper;
import com.tiemcheit.tiemcheitbe.model.Role;
import com.tiemcheit.tiemcheitbe.model.User;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserProfileResponse> getCustomersByStatus(String status, String sortOption, String order) {
        if (status == null && sortOption == null && order == null) {
            return allCustomer();
        }

        List<User> customers = new ArrayList<>();
        String uppercaseStatus = status != null ? status.toUpperCase() : null;
        if (sortOption != null) {
            switch (sortOption.toLowerCase()) {
                case "created_at":
                    if (order.equalsIgnoreCase("asc")) {
                        customers = userRepo.findAscCustomersOrderByCreatedAt(uppercaseStatus);
                    } else {
                        customers = userRepo.findDescCustomersOrderByCreatedAt(uppercaseStatus);
                    }
                    break;
                case "order_number":
                    if (order.equalsIgnoreCase("asc")) {
                        customers = userRepo.findAscCustomersOrderByOrderNumber(uppercaseStatus);
                    } else {
                        customers = userRepo.findDescCustomersOrderByOrderNumber(uppercaseStatus);
                    }
                    break;
                case "order_total":
                    if (order.equalsIgnoreCase("asc")) {
                        customers = userRepo.findAscCustomersOrderByOrderTotal(uppercaseStatus);
                    } else {
                        customers = userRepo.findDescCustomersOrderByOrderTotal(uppercaseStatus);
                    }
                    break;
            }
        } else {
            customers = userRepo.findCustomerByStatus(status);
        }
        List<UserProfileResponse> responseCustomers = new ArrayList<>();
        for (User c : customers) {
            responseCustomers.add(userMapper.toUserProfileResponse(c));
        }

        Role role = roleRepo.findByName("CUSTOMER").orElseThrow(() -> new AppException("Role not found.", HttpStatus.NOT_FOUND));
        for (UserProfileResponse u : responseCustomers) {
            if (u.getRoles().contains(roleMapper.toRoleResponse(role))) {
                u.setOrderNumber(orderRepo.countByUser_Id(u.getId()));
                u.setOrderTotal(orderRepo.getTotalAmountSpentByUser(u.getId()));
            }
        }
        return responseCustomers;
    }
}
