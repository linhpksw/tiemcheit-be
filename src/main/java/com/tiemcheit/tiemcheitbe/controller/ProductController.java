package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.ProductRequest;
import com.tiemcheit.tiemcheitbe.dto.request.UserReviewRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductDetailResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserReviewResponse;
import com.tiemcheit.tiemcheitbe.service.ProductService;
import com.tiemcheit.tiemcheitbe.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private static final String SUCCESS_MSG = "Success";
    private final ProductService productService;
    private final ReviewService reviewService;

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

    @GetMapping("filter")
    public ApiResponse<List<ProductResponse>> filter(
            @RequestParam Map<String, String> params,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction) {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getProductByConditionsAndSort(params, sortBy, direction))
                .message(SUCCESS_MSG)
                .build();
    }

    @GetMapping("pagination/{page}/{size}")
    public ApiResponse<Page<ProductResponse>> getProductsWithPagination(@PathVariable int page, @PathVariable int size) {
        return ApiResponse.<Page<ProductResponse>>builder()
                .data(productService.getProductsWithPagination(page, size))
                .message(SUCCESS_MSG)
                .build();
    }

    @GetMapping("pagination/{page}/{size}/filter")
    public ApiResponse<Page<ProductResponse>> getProductsWithPaginationAndFilter(@PathVariable int page, @PathVariable int size,
                                                                                 @RequestParam Map<String, String> params,
                                                                                 @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                                 @RequestParam(required = false, defaultValue = "asc") String direction) {
        return ApiResponse.<Page<ProductResponse>>builder()
                .data(productService.getProductsWithPaginationAndSort(page, size, params, sortBy, direction))
                .message(SUCCESS_MSG)
                .build();
    }


    @GetMapping("/{id}/reviews")
    public ApiResponse<List<UserReviewResponse>> getReviewsByProductId(@PathVariable("id") long productId) {
        return ApiResponse.<List<UserReviewResponse>>builder()
                .data(reviewService.getReviewsOfProduct(productId))
                .message("Success")
                .build();
    }

    @GetMapping("/top/{top}")
    public ApiResponse<List<ProductResponse>> getBestSellerProducts(@PathVariable int top) {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getTopBestsellers(top))
                .message(SUCCESS_MSG)
                .build();
    }

    @GetMapping("/ordered")
    public ApiResponse<List<ProductResponse>> getHistoryOrderedProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getHistoryOrderProduct())
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
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        return ApiResponse.<ProductResponse>builder()
                .data(productService.update(productRequest, id))
                .message(SUCCESS_MSG)
                .build();
    }

    @PutMapping("/{id}/reviews")
    public ApiResponse<UserReviewResponse> addReview(@PathVariable("id") long orderDetailId, @RequestBody UserReviewRequest userReviewRequest) {
        return ApiResponse.<UserReviewResponse>builder()
                .data(reviewService.addReview(orderDetailId, userReviewRequest))
                .message("Success")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteProduct(@PathVariable Long id) {
        return ApiResponse.<Boolean>builder()
                .data(productService.delete(id))
                .message(SUCCESS_MSG)
                .build();
    }

}
