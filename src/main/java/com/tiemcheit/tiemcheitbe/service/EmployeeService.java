package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.EmployeeRequest;
import com.tiemcheit.tiemcheitbe.dto.response.UserProfileResponse;
import com.tiemcheit.tiemcheitbe.mapper.RoleMapper;
import com.tiemcheit.tiemcheitbe.mapper.UserMapper;
import com.tiemcheit.tiemcheitbe.model.User;
import com.tiemcheit.tiemcheitbe.repository.OrderRepo;
import com.tiemcheit.tiemcheitbe.repository.RoleRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final OrderRepo orderRepo;
    private final UserMapper userMapper;
    private final UserService userService;
    private final RoleMapper roleMapper;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserProfileResponse> allEmployees(String status, Date startDate, Date endDate, String field, String order) {
//        List<UserProfileResponse> users = userService.getUsersProfile();
//        List<UserProfileResponse> employees = new ArrayList<>();
//        Role role = roleRepo.findByName("EMPLOYEE").orElseThrow(() -> new AppException("Role not found.", HttpStatus.NOT_FOUND));
//        for (UserProfileResponse u : users) {
//            if (u.getRoles().contains(roleMapper.toRoleResponse(role))) {
//                u.setOrderNumber(orderRepo.countByUser_Id(u.getId()));
//                u.setOrderTotal(orderRepo.getTotalAmountSpentByUser(u.getId()));
//                employees.add(u);
//            }
//        }
        List<User> employees = userRepo.findEmployees(status, startDate, endDate, field, order);
        List<UserProfileResponse> responses = new ArrayList<>();
        for (User e : employees) {
            responses.add(userMapper.toUserProfileResponse(e));
        }
        return responses;
    }

    public UserProfileResponse getEmployeeById(Long id) {
        return userMapper.toUserProfileResponse(userRepo.getReferenceById(id));
    }

    public UserProfileResponse updateEmployee(EmployeeRequest employeeRequest) {
        return userService.updateUserProfile(employeeRequest.getUsername(), employeeRequest.getUpdateData());
    }
}
