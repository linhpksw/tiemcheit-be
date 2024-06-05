package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.CartItemDeleteRequest;
import com.tiemcheit.tiemcheitbe.dto.request.CartItemRequest;
import com.tiemcheit.tiemcheitbe.dto.request.CartItemUpdateRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("")
    public ApiResponse<List<CartItemResponse>> allCartItems() {
        var data = cartService.allCartItems();
        return ApiResponse.<List<CartItemResponse>>builder()
                .message("Success")
                .data(data).build();
    }

    @PostMapping("")
    public ApiResponse<CartItemResponse> addToCart(@RequestBody CartItemRequest cartItemRequest) {
        var data = cartService.addToCart(cartItemRequest);
        return ApiResponse.<CartItemResponse>builder()
                .message("Success")
                .data(data).build();
    }

    @DeleteMapping("")
    public ApiResponse<Void> deleteCartItem(@RequestBody CartItemDeleteRequest cartItemDeleteRequest) {
        cartService.deleteCartItem(cartItemDeleteRequest);
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }

    @PatchMapping("")
    public ApiResponse<CartItemResponse> updateItemQuantity(@RequestBody CartItemUpdateRequest cartItemUpdateRequest) {
        var data = cartService.updateItemQuantity(cartItemUpdateRequest);
        return ApiResponse.<CartItemResponse>builder()
                .message("Success")
                .data(data).build();
    }
}
