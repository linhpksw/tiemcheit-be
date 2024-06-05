package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.WishlistItemRequest;
import com.tiemcheit.tiemcheitbe.dto.response.WishlistItemResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.WishlistItemMapper;
import com.tiemcheit.tiemcheitbe.model.WishlistItem;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import com.tiemcheit.tiemcheitbe.repository.WishlistRepo;
import com.tiemcheit.tiemcheitbe.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepo wishlistRepo;
    private final UserRepo userRepo;
    private final WishlistItemMapper wishlistItemMapper;

    public List<WishlistItemResponse> allWishlistItems() {
        List<WishlistItem> wishlistItems = wishlistRepo.findAll();
        List<WishlistItem> userWishlistItems = new ArrayList<>();

        for (WishlistItem wi : wishlistItems) {
            if (Objects.equals(wi.getUser().getUsername(), SecurityUtils.getCurrentUsername())) {
                userWishlistItems.add(wi);
            }
        }

        return wishlistItemMapper.toWishlistItemResponses(userWishlistItems);
    }

    public WishlistItemResponse addToWishlist(WishlistItemRequest wishlistItemRequest) {
        System.out.println(wishlistItemRequest.getProduct());

        List<WishlistItem> wishlistItems = wishlistRepo.findAll();
        if (!wishlistItems.isEmpty()) {
            for (WishlistItem wi : wishlistItems) {
                if (Objects.equals(wi.getUser().getUsername(), SecurityUtils.getCurrentUsername()) &&
                        Objects.equals(wi.getProduct().getId(), wishlistItemRequest.getProduct().getId())) {
                    System.out.println("Cannot add to wishlist the same product!");
                    return null;
                }
            }
        }

        WishlistItem wishlistItem = wishlistItemMapper.toEntity(wishlistItemRequest);
        wishlistItem.setUser(userRepo.findByUsername(SecurityUtils.getCurrentUsername()).orElseThrow(() -> new AppException("Order not found", HttpStatus.NOT_FOUND)));
        WishlistItem savedWishlistItem = wishlistRepo.save(wishlistItem);
        return wishlistItemMapper.toWishlistItemResponse(savedWishlistItem);
    }

    public void deleteWishlistItem(WishlistItemRequest wishlistItemRequest) {
        wishlistRepo.deleteByUserNameAndProductId(SecurityUtils.getCurrentUsername(), wishlistItemRequest.getProduct().getId());
    }

}
