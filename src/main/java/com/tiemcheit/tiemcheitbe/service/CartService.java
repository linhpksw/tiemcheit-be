package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CartItemRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.mapper.CartItemMapper;
import com.tiemcheit.tiemcheitbe.model.CartItem;
import com.tiemcheit.tiemcheitbe.repository.CartItemRepo;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final CartItemMapper cartItemMapper;

    public List<CartItemResponse> allCartItems(Long uid) {
        List<CartItem> cartItems = cartItemRepo.findAll();
        List<CartItem> userCartItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            if (Objects.equals(ci.getUser().getId(), uid)) {
                userCartItems.add(ci);
            }
        }

        return cartItemMapper.toCartItemResponses(userCartItems);
    }

    public CartItemResponse addToCart(CartItemRequest cartItemDto) {
        CartItem cartItem = cartItemMapper.toCartItem(cartItemDto);
        CartItem savedCartItem = cartItemRepo.save(cartItem);
        return cartItemMapper.toCartItemResponse(savedCartItem);
    }

    public void deleteCartItem(Long id) {
        if (cartItemRepo.existsById(id)) {
            cartItemRepo.deleteById(id);
        } else {
            System.out.println("No Cart Item has been found!");
        }
    }

}
