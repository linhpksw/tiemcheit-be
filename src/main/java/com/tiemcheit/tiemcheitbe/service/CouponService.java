package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CouponRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.dto.response.CouponResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.CouponMapper;
import com.tiemcheit.tiemcheitbe.model.*;
import com.tiemcheit.tiemcheitbe.repository.*;
import com.tiemcheit.tiemcheitbe.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponMapper couponMapper;

    private final CouponRepo couponRepository;

    private final CategoryRepo categoryRepository;

    private final ProductRepo productRepository;
    private final OrderRepo orderRepo;
    private final CartService cartService;
    private final UserRepo userRepo;

    @Transactional
    public List<CouponResponse> getAllCoupon() {
        return couponMapper.toResponses(couponRepository.findAllCoupon());
    }

    @Transactional
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code);
    }

    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        Coupon coupon = new Coupon();
        coupon.setCode(request.getCode());
        coupon.setName(request.getName());

        // check the valid date and expired date
        Date today = new Date();

        // Check if dateValid is greater than today's date
        if (request.getDateValid().before(today)) {
            throw new AppException("Date valid must be greater than today", HttpStatus.BAD_REQUEST);
        }
        coupon.setDateValid(request.getDateValid());


        // Check if dateExpired is greater than dateValid
        if (request.getDateExpired().before(request.getDateValid())) {
            throw new AppException("Date expired must be greater than date valid", HttpStatus.BAD_REQUEST);
        }
        coupon.setDateExpired(request.getDateExpired());

        coupon.setDescription(request.getDescription());

        if (request.getLimitUses() < request.getLimitAccountUses()) {
            throw new AppException("Limit uses must be greater than limit account uses", HttpStatus.BAD_REQUEST);
        }
        coupon.setLimitAccountUses(request.getLimitAccountUses());
        coupon.setLimitUses(request.getLimitUses());
        coupon.setStatus("inactive"); // Set other required fields
        coupon.setDateCreated(new Date()); // Example
        coupon.setDateUpdated(new Date()); // Example
        coupon.setUseCount(0); // Example


        Discount discount = new Discount();
        discount.setType(request.getDiscount().getType());
        discount.setValueType(request.getDiscount().getValueType());
        discount.setValueFixed(request.getDiscount().getValueFixed());

        switch (request.getDiscount().getType()) {
            case "category":
                Category category = categoryRepository.findById(request.getDiscount().getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
                discount.setCategory(category);
                break;
            case "product":
                Product product = productRepository.findById(request.getDiscount().getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
                discount.setProduct(product);
                break;
            case "total":
            case "ship":
                // No additional fields to set
                break;
            default:
                throw new IllegalArgumentException("Invalid discount type");
        }

        discount.setCoupon(coupon);


        coupon.setDiscount(discount);

        try {
            return couponMapper.toResponse(couponRepository.save(coupon));
        } catch (DataIntegrityViolationException e) {
            throw new AppException("Coupon code must be unique", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void activateCoupons(List<Long> couponIds) {
        List<Coupon> coupons = couponRepository.findAllById(couponIds);
        for (Coupon coupon : coupons) {
            coupon.setStatus("active");
        }
        couponRepository.saveAll(coupons);
    }

    @Transactional
    public void disableCoupons(Long id) {
        Coupon coupon = couponRepository.findById(id).get();
        coupon.setStatus("disabled");
        couponRepository.save(coupon);
    }


    public double applyCouponToCart(String code) {
        List<CartItemResponse> cartItemList = cartService.allCartItems();
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon == null) {
            throw new AppException("Coupon not found", HttpStatus.BAD_REQUEST);
        }
        // Check if the coupon has reached the total usage limit
        if (coupon.getUseCount() >= coupon.getLimitUses()) {
            throw new AppException("Coupon is not valid anymore", HttpStatus.BAD_REQUEST);
        }
        // Check if the user has reached the account usage limit for this coupon
        User user = userRepo.findByUsername(SecurityUtils.getCurrentUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        List<Order> orders = orderRepo.findByUserIdAndCouponId(user.getId(), coupon.getId());
        if (orders.size() >= coupon.getLimitAccountUses()) {
            throw new AppException("You have access the user's limit uses", HttpStatus.BAD_REQUEST);
        }

        String discountType = coupon.getDiscount().getType();
        double totalCost = 0;

        double totalDiscountAmount = 0.0;


        for (CartItemResponse item : cartItemList) {
            if (!discountType.equals("total")) {
                totalDiscountAmount += applyProductDiscount(coupon.getDiscount(), item.getProduct());
            }
            totalCost += item.getProduct().getPrice() * item.getQuantity();
        }

        if (discountType.equals("total")) {
            Discount discount = coupon.getDiscount();
            // apply for percent discount
            if ("percent".equalsIgnoreCase(discount.getValueType())) {
                totalDiscountAmount = totalCost * (discount.getValueFixed() / 100.0);
            }
            // apply for specific value discount
            else if ("fixed".equalsIgnoreCase(discount.getValueType())) {
                totalDiscountAmount += discount.getValueFixed();
            }
        }

        return totalDiscountAmount;
    }


    private double applyProductDiscount(Discount discount, ProductResponse product) {
        double discountAmount = 0.0;
        boolean canApply;

        // check product for discount
        if (product.getId().equals(discount.getProduct().getId())) {
            canApply = true;
        }

        // check product for discount
        else canApply = product.getCategory().getId().equals(discount.getCategory().getId());

        // apply discount
        if (canApply) {
            // apply for percent discount
            if ("percent".equalsIgnoreCase(discount.getValueType())) {
                discountAmount += product.getPrice() * (discount.getValueFixed() / 100.0);
            }
            // apply for specific value discount
            else if ("fixed".equalsIgnoreCase(discount.getValueType())) {
                discountAmount += discount.getValueFixed();
            }
        }

        return discountAmount;
    }

    @Transactional
    public void deleteCoupon(Long couponId) {
        if (!couponRepository.existsById(couponId)) {
            throw new AppException("Coupon with ID " + couponId + " does not exist", HttpStatus.BAD_REQUEST);
        }
        couponRepository.deleteById(couponId);
    }
}
