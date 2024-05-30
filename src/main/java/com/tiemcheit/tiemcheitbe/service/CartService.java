package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CartItemDeleteRequest;
import com.tiemcheit.tiemcheitbe.dto.request.CartItemRequest;
import com.tiemcheit.tiemcheitbe.dto.request.CartItemUpdateRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.CartItemMapper;
import com.tiemcheit.tiemcheitbe.model.CartItem;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.repository.CartItemRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import com.tiemcheit.tiemcheitbe.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepo cartItemRepo;
    private final UserRepo userRepo;
    private final CartItemMapper cartItemMapper;
    private final UserService userService;

//    @Autowired
//    public CartService(CartItemRepo cartItemRepo, CartItemMapper cartItemMapper, UserService userService) {
//        this.cartItemRepo = cartItemRepo;
//        this.cartItemMapper = cartItemMapper;
//        this.userService = userService;
//    }

    public List<CartItemResponse> allCartItems() {
        List<CartItem> cartItems = cartItemRepo.findAll();
        List<CartItem> userCartItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            if (Objects.equals(ci.getUser().getUsername(), SecurityUtils.getCurrentUsername())) {
                userCartItems.add(ci);
            }
        }

        return cartItemMapper.toCartItemResponses(userCartItems);
    }

    public CartItemRequest addToCart(CartItemRequest cartItemRequest) {
        List<CartItem> cartItems = cartItemRepo.findAll();
        for (CartItem ci : cartItems) {
            if (Objects.equals(ci.getUser().getUsername(), SecurityUtils.getCurrentUsername()) &&
                    Objects.equals(ci.getProduct().getId(), cartItemRequest.getProduct().getId())) {
                System.out.println("Cannot add to cart the same product!");
                return null;
            }
        }

        CartItem cartItem = cartItemMapper.toEntity(cartItemRequest);
        cartItem.setUser(userRepo.findByUsername(SecurityUtils.getCurrentUsername()).orElseThrow(() -> new AppException("Order not found", HttpStatus.NOT_FOUND)));
        CartItem savedCartItem = cartItemRepo.save(cartItem);
        return cartItemMapper.toCartItemRequest(savedCartItem);
    }

    public void deleteCartItem(CartItemDeleteRequest cartItemDeleteRequest) {
        if (cartItemRepo.existsById(cartItemDeleteRequest.getId())) {
            cartItemRepo.deleteById(cartItemDeleteRequest.getId());
        } else {
            System.out.println("No Cart Item has been found!");
        }
    }

    public CartItemResponse updateItemQuantity(CartItemUpdateRequest cartItemUpdateRequest) {
        CartItem updatedCartItem = cartItemMapper.toEntity(cartItemUpdateRequest);
        updatedCartItem.setUser(userRepo.findByUsername(SecurityUtils.getCurrentUsername()).orElseThrow(() -> new AppException("Order not found", HttpStatus.NOT_FOUND)));
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
