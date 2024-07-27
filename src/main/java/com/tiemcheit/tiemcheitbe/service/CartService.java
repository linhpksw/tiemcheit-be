package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CartItemDeleteRequest;
import com.tiemcheit.tiemcheitbe.dto.request.CartItemRequest;
import com.tiemcheit.tiemcheitbe.dto.request.CartItemUpdateRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.mapper.CartItemMapper;
import com.tiemcheit.tiemcheitbe.model.CartItem;
import com.tiemcheit.tiemcheitbe.model.Ingredient;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.model.ProductIngredient;
import com.tiemcheit.tiemcheitbe.repository.CartItemRepo;
import com.tiemcheit.tiemcheitbe.repository.ProductIngredientRepo;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import com.tiemcheit.tiemcheitbe.repository.exception.AppException;
import com.tiemcheit.tiemcheitbe.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepo cartItemRepo;
    private final UserRepo userRepo;
    private final CartItemMapper cartItemMapper;
    private final ProductRepo productRepo;
    private final ProductIngredientRepo productIngredientRepo;

    public List<CartItemResponse> allCartItemsFromUsername(String username) {
        List<CartItem> cartItems = cartItemRepo.findAll();
        List<CartItem> userCartItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            if (ci.getUser().getUsername().equals(username)) {
                userCartItems.add(ci);
            }
        }

        return cartItemMapper.toCartItemResponses(userCartItems);
    }

    public List<CartItemResponse> allCartItems() {
        List<CartItem> cartItems = cartItemRepo.findAll();
        List<CartItem> userCartItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            if (ci.getUser().getUsername().equals(SecurityUtils.getCurrentUsername())) {
                userCartItems.add(ci);
            }
        }

        return cartItemMapper.toCartItemResponses(userCartItems);
    }

    public CartItemResponse addToCart(CartItemRequest cartItemRequest) {
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
        return cartItemMapper.toCartItemResponse(savedCartItem);
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

    public void clearCart(String username) {
        cartItemRepo.deleteByUserName(username);
    }

    // add validate ingredient available
    @Transactional
    public void checkIngredientsAvailability(List<CartItemRequest> cartItemRequests) {
        for (CartItemRequest cartItemRequest : cartItemRequests) {
            List<ProductIngredient> productIngredients = productIngredientRepo.findAllByProductIdWithLock(cartItemRequest.getProduct().getId());
            for (ProductIngredient productIngredient : productIngredients) {
                Ingredient ingredient = productIngredient.getIngredient();
                float unitsNeeded = productIngredient.getUnit() * cartItemRequest.getQuantity();

                if (ingredient.getQuantity() < unitsNeeded) {
                    throw new AppException("Không đủ nguyên liệu cho " + productIngredient.getProduct().getName(), HttpStatus.BAD_REQUEST);
                }
            }
        }
    }
}
