package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.IngredientResponse;
import com.tiemcheit.tiemcheitbe.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredient")
public class IngredientController {
    private static final String SUCCESS_MSG = "Success";
    private final IngredientService ingredientService;

    @GetMapping("")
    public ApiResponse<List<IngredientResponse>> getAllIngredients() {
        return ApiResponse.<List<IngredientResponse>>builder()
                .data(ingredientService.getAllIngredients())
                .message(SUCCESS_MSG)
                .build();
    }
}
