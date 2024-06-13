package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.OptionResponse;
import com.tiemcheit.tiemcheitbe.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/option")
public class OptionController {
    private static final String SUCCESS_MSG = "Success";
    private final OptionService optionService;

    @GetMapping("")
    public ApiResponse<List<OptionResponse>> getAllOptions() {
        return ApiResponse.<List<OptionResponse>>builder()
                .data(optionService.getAllOptions())
                .message(SUCCESS_MSG)
                .build();
    }

}
