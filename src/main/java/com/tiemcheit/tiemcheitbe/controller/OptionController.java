package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.OptionRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.OptionResponse;
import com.tiemcheit.tiemcheitbe.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/options")
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

    @PostMapping("")
    public ApiResponse<OptionResponse> createOption(@RequestBody OptionRequest optionRequest) {
        return ApiResponse.<OptionResponse>builder()
                .data(optionService.create(optionRequest))
                .message(SUCCESS_MSG)
                .build();
    }
}
