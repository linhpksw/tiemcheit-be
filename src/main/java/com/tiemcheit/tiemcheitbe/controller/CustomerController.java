package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.CustomerRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.CustomerResponse;
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
    public ApiResponse<List<CustomerResponse>> allCustomers() {
        var data = customerService.allCustomer();
        return ApiResponse.<List<CustomerResponse>>builder()
                .message("Success")
                .data(data).build();
    }

    @GetMapping("/{uid}")
    public ApiResponse<CustomerResponse> getCustomerInfo(@PathVariable Long uid) {
        var data = customerService.getCustomerById(uid);
        return ApiResponse.<CustomerResponse>builder()
                .message("Success")
                .data(data).build();
    }

    @PatchMapping("")
    public ApiResponse<CustomerResponse> editCustomerIndo(@RequestBody CustomerRequest customerRequest) {
        var data = customerService.updateCustomer(customerRequest);
        return ApiResponse.<CustomerResponse>builder()
                .message("Success")
                .data(data).build();
    }
}
