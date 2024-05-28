package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.CartItemRequest;
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

    @PostMapping("/cart/add")
    public ResponseEntity<CartItemResponse> addToCart(@RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    @DeleteMapping("/cart/delete/id")
    public void deleteCartItem(@PathVariable Long id) {
        cartService.deleteCartItem(id);
    }

}
