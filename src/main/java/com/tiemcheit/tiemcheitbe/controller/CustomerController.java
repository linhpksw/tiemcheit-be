package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.CustomerResponse;
import com.tiemcheit.tiemcheitbe.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/customer")
public class CustomerController {

    public final CustomerService customerService;

    @GetMapping("")
    public ApiResponse<List<CustomerResponse>> allCustomers() {
        var data = customerService.allCustomer();
        return ApiResponse.<List<CustomerResponse>>builder()
                .message("Success")
                .data(data).build();
    }
}
