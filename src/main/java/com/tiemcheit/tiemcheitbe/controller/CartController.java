package com.tiemcheit.tiemcheitbe.controller;

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

//    @Autowired
//    public CartController(CartService cartService) {
//        this.cartService = cartService;
//    }

    @GetMapping("/{uid}")
    public ApiResponse<List<CartItemResponse>> allCartItems(@PathVariable Long uid) {
        var data = cartService.allCartItems(uid);
        return ApiResponse.<List<CartItemResponse>>builder()
                .message("Success")
                .data(data).build();
    }

    @PostMapping("/{uid}")
    public ApiResponse<CartItemRequest> addToCart(@RequestBody CartItemRequest cartItemDto, @PathVariable Long uid) {
        var data = cartService.addToCart(cartItemDto, uid);
        return ApiResponse.<CartItemRequest>builder()
                .message("Success")
                .data(data).build();
    }

    @DeleteMapping("/{cid}")
    public ApiResponse<Void> deleteCartItem(@PathVariable Long cid) {
        cartService.deleteCartItem(cid);
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }

    @PatchMapping("/{uid}")
    public ApiResponse<CartItemResponse> updateItemQuantity(@RequestBody CartItemUpdateRequest cartItemUpdateRequest, @PathVariable Long uid) {
        var data = cartService.updateItemQuantity(cartItemUpdateRequest, uid);
        return ApiResponse.<CartItemResponse>builder()
                .message("Success")
                .data(data).build();
    }
}
