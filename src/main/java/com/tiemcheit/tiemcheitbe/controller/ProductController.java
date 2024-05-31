package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductDetailResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    // this request is:  http://localhost:8080/product/getAll
    @GetMapping("/getAll")
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProducts())
                .message("Success")
                .build();
    }

    // this request is:  http://localhost:8080/product/getAllByCategory/{id}
    @GetMapping("/getAllByCategory/{id}")
    public ApiResponse<List<ProductResponse>> getAllProductsByCategoryID(@PathVariable Long id) {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProductsByCategoryId(id))
                .message("Success")
                .build();
    }

    // this request is:  http://localhost:8080/product/getDetail/{id}
    @GetMapping("/getDetail/{id}")
    public ApiResponse<ProductDetailResponse> getProductDetailById(@PathVariable Long id) {
        return ApiResponse.<ProductDetailResponse>builder()
                .data(productService.getProductDetailById(id))
                .message("Success")
                .build();
    }


}
