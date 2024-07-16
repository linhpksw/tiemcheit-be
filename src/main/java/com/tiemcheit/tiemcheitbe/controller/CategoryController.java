package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.CategoryRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.CategoryResponse;
import com.tiemcheit.tiemcheitbe.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {
    private static final String SUCCESS_MSG = "Success";
    private final CategoryService categoryService;

    @GetMapping("")
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getAllCategories())
                .message(SUCCESS_MSG)
                .build();
    }

    @GetMapping("/status/active/client")
    public ApiResponse<List<CategoryResponse>> getAllCategoriesByActiveStatus() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getAllCategoriesByActiveStatus())
                .message(SUCCESS_MSG)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.getCategoryById(id))
                .message(SUCCESS_MSG)
                .build();
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<CategoryResponse>> getAllCategoriesByStatus(@PathVariable String status) {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getAllCategoriesByStatus(status))
                .message(SUCCESS_MSG)
                .build();
    }

    @PostMapping("")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest category) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.create(category))
                .message(SUCCESS_MSG)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest category) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.update(id, category))
                .message(SUCCESS_MSG)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteCategory(@PathVariable Long id) {
        return ApiResponse.<Boolean>builder()
                .data(categoryService.delete(id))
                .message(SUCCESS_MSG)
                .build();
    }

    @PutMapping("/{id}/status/{status}/{type}") // type is restore or all
    public ApiResponse<CategoryResponse> updateCategoryStatus(@PathVariable Long id, @PathVariable String status, @PathVariable String type) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.updateCategoryStatus(id, status, type))
                .message(SUCCESS_MSG)
                .build();
    }


}
