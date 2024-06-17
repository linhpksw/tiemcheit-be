package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.CustomerRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserProfileResponse;
import com.tiemcheit.tiemcheitbe.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/customers")
public class CustomerController {

    public final CustomerService customerService;

    @GetMapping("")
    public ApiResponse<List<UserProfileResponse>> allCustomers() {
        var data = customerService.allCustomer();
        return ApiResponse.<List<UserProfileResponse>>builder()
                .message("Success")
                .data(data).build();
    }

    @GetMapping("/{uid}")
    public ApiResponse<UserProfileResponse> getCustomerInfo(@PathVariable Long uid) {
        var data = customerService.getCustomerById(uid);
        return ApiResponse.<UserProfileResponse>builder()
                .message("Success")
                .data(data).build();
    }

    @PatchMapping("")
    public ApiResponse<UserProfileResponse> editCustomerIndo(@RequestBody CustomerRequest customerRequest) {
        var data = customerService.updateCustomer(customerRequest);
        return ApiResponse.<UserProfileResponse>builder()
                .message("Success")
                .data(data).build();
    }
}
