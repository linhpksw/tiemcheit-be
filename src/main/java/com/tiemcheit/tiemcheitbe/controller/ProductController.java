package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.ProductRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductDetailResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
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

    @GetMapping("/status/active-disabled")
    public ApiResponse<List<ProductResponse>> getAllProductsByActiveAndDisabledStatus() {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProductsByActiveAndDisabledStatus())
                .message(SUCCESS_MSG)
                .build();
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<ProductResponse>> getAllProductsByStatus(@PathVariable String status) {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProductsByStatus(status))
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

    @GetMapping("/top/{top}")
    public ApiResponse<List<ProductResponse>> getBestSellerProducts(@PathVariable int top) {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getTopBestsellers(top))
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

    @GetMapping("/filter")
    public List<ProductResponse> searchProducts(
            @RequestParam Map<String, String> params,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction) {
        return productService.getProductByConditionsAndSort(params, sortBy, direction);
    }
}
