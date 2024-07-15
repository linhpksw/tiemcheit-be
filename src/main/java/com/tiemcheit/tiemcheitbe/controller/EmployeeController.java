package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.EmployeeRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserProfileResponse;
import com.tiemcheit.tiemcheitbe.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/employees")
public class EmployeeController {

    public final EmployeeService employeeService;

    @GetMapping("")
    public ApiResponse<List<UserProfileResponse>> allCustomers(
            @RequestParam("status") String status, @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate, @RequestParam("field") String field,
            @RequestParam("order") String order) {
        var data = employeeService.allEmployees(status, startDate, endDate, field, order);
        return ApiResponse.<List<UserProfileResponse>>builder()
                .message("Success")
                .data(data).build();
    }

    @GetMapping("/{uid}")
    public ApiResponse<UserProfileResponse> getCustomerInfo(@PathVariable Long uid) {
        var data = employeeService.getEmployeeById(uid);
        return ApiResponse.<UserProfileResponse>builder()
                .message("Success")
                .data(data).build();
    }

    @PatchMapping("")
    public ApiResponse<UserProfileResponse> editCustomerInfo(@RequestBody EmployeeRequest employeeRequest) {
        var data = employeeService.updateEmployee(employeeRequest);
        return ApiResponse.<UserProfileResponse>builder()
                .message("Success")
                .data(data).build();
    }

//    @GetMapping("/filter")
//    public ApiResponse<List<UserProfileResponse>> filteredCustomers(
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) String sortOption,
//            @RequestParam(required = false) String order) {
//        var data = customerService.getCustomersByStatus(status, sortOption, order);
//        return ApiResponse.<List<UserProfileResponse>>builder()
//                .message("Success")
//                .data(data)
//                .build();
//    }

}
