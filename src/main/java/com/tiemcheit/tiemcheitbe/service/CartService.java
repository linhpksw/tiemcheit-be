package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CartItemRequest;
import com.tiemcheit.tiemcheitbe.dto.request.CartItemUpdateRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.mapper.CartItemMapper;
import com.tiemcheit.tiemcheitbe.model.CartItem;
import com.tiemcheit.tiemcheitbe.model.Product;
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
    private final CartItemMapper cartItemMapper;
    private final UserService userService;

//    @Autowired
//    public CartService(CartItemRepo cartItemRepo, CartItemMapper cartItemMapper, UserService userService) {
//        this.cartItemRepo = cartItemRepo;
//        this.cartItemMapper = cartItemMapper;
//        this.userService = userService;
//    }

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

    public CartItemRequest addToCart(CartItemRequest cartItemRequest, Long uid) {
        List<CartItem> cartItems = cartItemRepo.findAll();
        for (CartItem ci : cartItems) {
            if (Objects.equals(ci.getUser().getId(), uid) &&
                    Objects.equals(ci.getProduct().getId(), cartItemRequest.getProduct().getId())) {
                System.out.println("Cannot add to cart the same product!");
                return null;
            }
        }

        CartItem cartItem = cartItemMapper.toEntity(cartItemRequest);
        cartItem.setUser(userService.getById(uid));
        CartItem savedCartItem = cartItemRepo.save(cartItem);
        return cartItemMapper.toCartItemRequest(savedCartItem);
    }

    public void deleteCartItem(Long id) {
        if (cartItemRepo.existsById(id)) {
            cartItemRepo.deleteById(id);
        } else {
            System.out.println("No Cart Item has been found!");
        }
    }

    public CartItemResponse updateItemQuantity(CartItemUpdateRequest cartItemUpdateRequest, Long uid) {
        CartItem updatedCartItem = cartItemMapper.toEntity(cartItemUpdateRequest);
        updatedCartItem.setUser(userService.getById(uid));
        updatedCartItem.setProduct(getProductInCartItem(cartItemUpdateRequest.getId()));
        cartItemRepo.save(updatedCartItem);

        return cartItemMapper.toCartItemResponse(updatedCartItem);
    }

    private Product getProductInCartItem(Long id) {
        return cartItemRepo.getReferenceById(id).getProduct();
    }

    public void clearCart(Long uid) {
        cartItemRepo.deleteByUserId(uid);
    }
}
