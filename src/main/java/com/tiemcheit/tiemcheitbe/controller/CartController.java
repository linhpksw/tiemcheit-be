package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.CartItemDto;
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
    public ResponseEntity<List<CartItemDto>> allCartItems(@PathVariable Long uid) {
        return ResponseEntity.ok(cartService.allCartItems(uid));
    }

    @PostMapping("/cart")
    public ResponseEntity<CartItemDto> addToCart(@RequestBody CartItemDto cartItemDto) {
        return ResponseEntity.ok(cartService.addToCart(cartItemDto));
    }

    @DeleteMapping("/cart/{cid}")
    public void deleteCartItem(@PathVariable Long cid) {
        cartService.deleteCartItem(cid);
    }

}
