package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.WishlistItemRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.WishlistItemResponse;
import com.tiemcheit.tiemcheitbe.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("")
    public ApiResponse<List<WishlistItemResponse>> allWishlistItems() {
        var data = wishlistService.allWishlistItems();
        return ApiResponse.<List<WishlistItemResponse>>builder()
                .message("Success")
                .data(data).build();
    }

    @PostMapping("")
    public ApiResponse<WishlistItemResponse> addToWishlist(@RequestBody WishlistItemRequest wishlistItemRequest) {
        var data = wishlistService.addToWishlist(wishlistItemRequest);
        return ApiResponse.<WishlistItemResponse>builder()
                .message("Success")
                .data(data).build();
    }

    @DeleteMapping("")
    public ApiResponse<Void> deleteCartItem(@RequestBody WishlistItemRequest wishlistItemRequest) {
        wishlistService.deleteWishlistItem(wishlistItemRequest);
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }
}
