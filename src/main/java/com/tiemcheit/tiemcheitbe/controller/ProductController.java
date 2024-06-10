package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.ProductRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductDetailResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {
    private static final String SUCCESS_MSG = "Success";
    private final ProductService productService;

    @GetMapping("")
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProducts())
                .message(SUCCESS_MSG)
                .build();
    }

    @GetMapping("/category/{id}")
    public ApiResponse<List<ProductResponse>> getAllProductsByCategoryID(@PathVariable Long id) {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProductsByCategoryId(id))
                .message(SUCCESS_MSG)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDetailResponse> getProductDetailById(@PathVariable Long id) {
        return ApiResponse.<ProductDetailResponse>builder()
                .data(productService.getProductDetailById(id))
                .message(SUCCESS_MSG)
                .build();
    }

    @PostMapping("")
    public ApiResponse<ProductResponse> addProduct(@RequestBody ProductRequest productRequest) {
        return ApiResponse.<ProductResponse>builder()
                .data(productService.create(productRequest))
                .message(SUCCESS_MSG)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable Long id) {
        return ApiResponse.<ProductResponse>builder()
                .data(productService.update(productRequest, id))
                .message(SUCCESS_MSG)
                .build();
    }
}
