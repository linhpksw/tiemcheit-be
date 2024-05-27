package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.AddCartItemDto;
import com.tiemcheit.tiemcheitbe.dto.GetCartItemDto;
import com.tiemcheit.tiemcheitbe.mapper.AddCartItemMapper;
import com.tiemcheit.tiemcheitbe.mapper.GetCartItemMapper;
import com.tiemcheit.tiemcheitbe.model.CartItem;
import com.tiemcheit.tiemcheitbe.repository.CartItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepo cartItemRepo;
    private final GetCartItemMapper getCartItemMapper;
    private final AddCartItemMapper addCartItemMapper;

    public List<GetCartItemDto> allCartItems(Long uid) {
        List<CartItem> cartItems = cartItemRepo.findAll();
        List<CartItem> userCartItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            if (Objects.equals(ci.getUser().getId(), uid)) {
                userCartItems.add(ci);
            }
        }

        return getCartItemMapper.toDtos(userCartItems);
    }

    public AddCartItemDto addToCart(AddCartItemDto cartItemDto) {
        List<CartItem> cartItems = cartItemRepo.findAll();
        for (CartItem ci : cartItems) {
            if (Objects.equals(ci.getProduct().getId(), cartItemDto.getProduct().getId())) {
                System.out.println("Cannot add to cart the same product!");
                return null;
            }
        }

        CartItem cartItem = addCartItemMapper.toEntity(cartItemDto);
        CartItem savedCartItem = cartItemRepo.save(cartItem);
        return addCartItemMapper.toDto(savedCartItem);
    }

    public void deleteCartItem(Long id) {
        if (cartItemRepo.existsById(id)) {
            cartItemRepo.deleteById(id);
        } else {
            System.out.println("No Cart Item has been found!");
        }
    }

}
