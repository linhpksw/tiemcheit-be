package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.service.RevenueService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/revenue")
@AllArgsConstructor
public class RevenueController {
    private static final String SUCCESS_MSG = "Success";

    private final RevenueService revenueService;

    @GetMapping("")
    public ApiResponse<Double> getRevenue() {
        return ApiResponse.<Double>builder()
                .data(revenueService.getRevenue())
                .message(SUCCESS_MSG)
                .build();
    }

}
