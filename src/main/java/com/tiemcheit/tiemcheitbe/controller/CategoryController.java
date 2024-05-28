package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.CategoryResponse;
import com.tiemcheit.tiemcheitbe.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    // this request is:  http://localhost:8080/category/getAll
    @GetMapping("/getAll")
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getAllCategories())
                .message("Success")
                .build();
    }
}
