package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.IngredientRequest;
import com.tiemcheit.tiemcheitbe.dto.request.IngredientRestockRequest;
import com.tiemcheit.tiemcheitbe.dto.request.RoleRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.IngredientResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.dto.response.RoleResponse;
import com.tiemcheit.tiemcheitbe.service.IngredientService;
import com.tiemcheit.tiemcheitbe.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;
    private final ProductService productService;
    @GetMapping("")
    public ApiResponse<List<IngredientResponse>> getAllIngredients() {
        return ApiResponse.<List<IngredientResponse>>builder()
                .data(ingredientService.getAllIngredients())
                .message("Success")
                .build();
    }
    @GetMapping("/{id}/products")
    public ApiResponse<List<ProductResponse>> getAllProductsOfIngredient(@PathVariable long id){
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getProductsOfIngredient(id))
                .message("success")
                .build();
    }
    @PostMapping("")
    public ApiResponse<IngredientResponse> create(@RequestBody IngredientRequest ingredientRequest){
        return ApiResponse.<IngredientResponse>builder()
                .data(ingredientService.create(ingredientRequest))
                .message("Success")
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<IngredientResponse> getById(@PathVariable long id){
        return ApiResponse.<IngredientResponse>builder()
                .data(ingredientService.getIngredientById(id))
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
    @GetMapping("pagination/{page}/{size}/filter")
    public ApiResponse<Page<IngredientResponse>> getIngredientsWithPaginationAndFilter(
            @PathVariable int page,
            @PathVariable int size,
            @RequestParam Map<String, String> params,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction) {
        return ApiResponse.<Page<IngredientResponse>>builder()
                .data(ingredientService.getIngredientsWithPaginationAndSort(page, size, params, sortBy, direction))
                .message("Success")
                .build();
    }


}
