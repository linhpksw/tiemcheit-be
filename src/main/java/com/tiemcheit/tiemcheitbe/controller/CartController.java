package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.CartItemRequest;
import com.tiemcheit.tiemcheitbe.dto.request.CartItemUpdateRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart/{uid}")
    public ResponseEntity<List<CartItemResponse>> allCartItems(@PathVariable Long uid) {
        return ResponseEntity.ok(cartService.allCartItems(uid));
    }

    @PostMapping("/cart/{uid}")
    public ResponseEntity<CartItemRequest> addToCart(@RequestBody CartItemRequest cartItemDto, @PathVariable Long uid) {
        return ResponseEntity.ok(cartService.addToCart(cartItemDto, uid));
    }

    @DeleteMapping("/cart/{cid}")
    public void deleteCartItem(@PathVariable Long cid) {
        cartService.deleteCartItem(cid);
    }

    @PatchMapping("/cart/{uid}")
    public ResponseEntity<List<CartItemResponse>> updateItemQuantity(@RequestBody CartItemUpdateRequest cartItemUpdateRequest, @PathVariable Long uid) {
        cartService.updateItemQuantity(cartItemUpdateRequest, uid);
        return allCartItems(uid);
    }
}
