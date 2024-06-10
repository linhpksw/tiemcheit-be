package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.IngredientRequest;
import com.tiemcheit.tiemcheitbe.dto.request.IngredientRestockRequest;
import com.tiemcheit.tiemcheitbe.dto.request.RoleRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.IngredientResponse;
import com.tiemcheit.tiemcheitbe.dto.response.RoleResponse;
import com.tiemcheit.tiemcheitbe.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;
    @GetMapping("")
    public ApiResponse<List<IngredientResponse>> getAllIngredients() {
        return ApiResponse.<List<IngredientResponse>>builder()
                .data(ingredientService.getAllIngredients())
                .message("Success")
                .build();
    }
    @PostMapping("")
    public ApiResponse<IngredientResponse> create(@RequestBody IngredientRequest ingredientRequest){
        return ApiResponse.<IngredientResponse>builder()
                .data(ingredientService.create(ingredientRequest))
                .message("Success")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<IngredientResponse> update(@PathVariable long id, @RequestBody IngredientRequest ingredientRequest){
        return ApiResponse.<IngredientResponse>builder()
                .data(ingredientService.update(id, ingredientRequest))
                .message("Success")
                .build();
    }
    @PatchMapping("/{id}/disable")
    public ApiResponse<IngredientResponse> disable(@PathVariable long id, @RequestBody IngredientRequest ingredientRequest){
        return ApiResponse.<IngredientResponse>builder()
                .data(ingredientService.disable(id, ingredientRequest))
                .message("Success")
                .build();
    }
    @PatchMapping("/{id}/restock")
    public ApiResponse<IngredientResponse> restock(@PathVariable long id, @RequestBody IngredientRestockRequest ingredientRequest) {
        return ApiResponse.<IngredientResponse>builder()
                .data(ingredientService.restock(id, ingredientRequest))
                .message("Success")
                .build();
    }
}
