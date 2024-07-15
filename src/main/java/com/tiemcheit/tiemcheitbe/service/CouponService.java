package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CouponRequest;
import com.tiemcheit.tiemcheitbe.dto.request.DiscountRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.dto.response.CouponResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.mapper.CouponMapper;
import com.tiemcheit.tiemcheitbe.model.*;
import com.tiemcheit.tiemcheitbe.repository.*;
import com.tiemcheit.tiemcheitbe.repository.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
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
    private final EmailService emailService;
    private final DiscountRepo discountRepo;

    @Transactional
    public List<CouponResponse> getAllCoupon() {
        return couponMapper.toResponses(couponRepository.findAllCoupon());
    }

    @Transactional
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code);
    }

    @Transactional
    public CouponResponse getCouponById(Long id) {
        if (couponRepository.findById(id).isPresent()) {
            return couponMapper.toResponse(couponRepository.findById(id).get());
        } else {
            return null;
        }
    }

    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        validateCouponRequest(request);

        Coupon coupon = new Coupon();
        coupon.setCode(request.getCode());
        coupon.setName(request.getName());
        coupon.setDateValid(request.getDateValid());
        coupon.setDateExpired(request.getDateExpired());
        coupon.setDescription(request.getDescription());
        coupon.setLimitAccountUses(request.getLimitAccountUses());
        coupon.setLimitUses(request.getLimitUses());
        coupon.setStatus("inactive"); // Set other required fields
        coupon.setDateCreated(new Date()); // Example
        coupon.setDateUpdated(new Date()); // Example
        coupon.setUseCount(0); // Example


        List<Discount> discounts = request.getDiscounts().stream().map(discountItem -> {
            Discount discount = new Discount();
            discount.setType(discountItem.getType());
            discount.setValueType(discountItem.getValueType());
            discount.setValueFixed(discountItem.getValueFixed());

            switch (discountItem.getType()) {
                case "category":
                    Category category = categoryRepository.findById(discountItem.getCategoryId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
                    discount.setCategory(category);
                    break;
                case "product":
                    Product product = productRepository.findById(discountItem.getProductId())
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
            return discount;
        }).collect(Collectors.toList());

        coupon.setDiscounts(discounts);

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


    public double applyCouponToCart(String code, String username) {
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
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<Order> orders = orderRepo.findByUserIdAndCouponId(user.getId(), coupon.getId());

        if (orders.size() >= coupon.getLimitAccountUses()) {
            throw new AppException("You have access the user's limit uses", HttpStatus.BAD_REQUEST);
        }

        String discountType = coupon.getDiscounts().getFirst().getType();
        double totalCost = 0;

        double totalDiscountAmount = 0.0;


        for (CartItemResponse item : cartItemList) {
            if (!discountType.equals("total")) {
                totalDiscountAmount += applyProductDiscount(coupon.getDiscounts(), item.getProduct());
            }
            totalCost += item.getProduct().getPrice() * item.getQuantity();
        }

        if (discountType.equals("total")) {
            Discount discount = coupon.getDiscounts().getFirst();
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


    public double applyProductDiscount(List<Discount> discounts, ProductResponse product) {
        double discountAmount = 0.0;
        boolean canApply = false;

        for (Discount discount : discounts) {
            // check product for discount
            if (discount.getProduct() != null &&
                    product.getId().equals(discount.getProduct().getId())) {
                canApply = true;
            }

            // check product for discount
            if (discount.getCategory() != null && product.getCategory().getId().equals(discount.getCategory().getId())) {
                canApply = true;
            }
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

    // Method to update the coupon status
    public void updateCouponStatus() {
        List<Coupon> coupons = couponRepository.findAll();
        Date now = new Date();

        for (Coupon coupon : coupons) {
            boolean canUpdate = false;
            if (!coupon.getStatus().equals("disable") && coupon.getDateExpired().compareTo(now) <= 0) {
                coupon.setStatus("disabled");
                canUpdate = true;
            } else if (!coupon.getStatus().equals("active") && coupon.getDateValid().compareTo(now) <= 0
                    && coupon.getDateExpired().compareTo(now) >= 0) {
                coupon.setStatus("active");
                canUpdate = true;
            } else if (!coupon.getStatus().equals("inactive")) {
                coupon.setStatus("inactive");
                canUpdate = true;
            }
            if (canUpdate) couponRepository.save(coupon);
        }
    }

    // Scheduled task to run the updateCouponStatus method every day at midnight
    @Scheduled(cron = "0 * * * * ?")
    public void scheduleCouponStatusUpdate() {
        updateCouponStatus();
    }

    public void validateCouponRequest(CouponRequest request) {
        if (request.getCode() == null || !validateCode(request.getCode())) {
            throw new AppException("Mã không hợp lệ", HttpStatus.BAD_REQUEST);
        }
        if (request.getName() == null || !validateName(request.getName())) {
            throw new AppException("Tên không hợp lệ", HttpStatus.BAD_REQUEST);
        }
        if (request.getDateValid() == null || request.getDateValid().before(new Date())) {
            throw new AppException("Date Valid is required and must be in the future", HttpStatus.BAD_REQUEST);
        }
        if (request.getDateExpired() == null || request.getDateExpired().before(request.getDateValid())) {
            throw new AppException("Date Expired is required and must be after Date Valid", HttpStatus.BAD_REQUEST);
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new AppException("Vui lòng nhập mô tả", HttpStatus.BAD_REQUEST);
        }
        if (request.getLimitAccountUses() < 1) {
            throw new AppException("Limit Account Uses is required and must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        if (request.getLimitUses() < 1) {
            throw new AppException("Limit Uses is required and must be greater than 0", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean validateName(String name) {
        String trimmedValue = name.trim();
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9 ]+$");
        return pattern.matcher(trimmedValue).matches() &&
                !trimmedValue.contains("  ") &&
                trimmedValue.length() >= 4 &&
                trimmedValue.length() <= 64 &&
                name.equals(trimmedValue);
    }

    private boolean validateCode(String code) {
        String trimmedValue = code.trim();
        Pattern pattern = Pattern.compile("^[A-Z0-9]+$");
        return pattern.matcher(trimmedValue).matches() &&
                trimmedValue.length() >= 4 &&
                trimmedValue.length() <= 64 &&
                code.equals(trimmedValue);
    }

    public void sendCouponCode(List<String> emails, String code) {
        for (String email : emails) {
            User user = userRepo.findByEmail(email).get();
            Coupon coupon = couponRepository.findByCode(code);
            emailService.sendCouponCode(user, coupon);
        }
    }

    public void updateCoupon(Long id, CouponRequest request) {
        Optional<Coupon> optionalCoupon = couponRepository.findById(id);

        if (optionalCoupon.isPresent()) {
            Coupon coupon = optionalCoupon.get();
            coupon.setName(request.getName());
            coupon.setCode(request.getCode());
            coupon.setDateExpired(request.getDateExpired());
            coupon.setDateValid(request.getDateValid());
            coupon.setDescription(request.getDescription());
            List<DiscountRequest> discountUpdateRequests = request.getDiscounts();
            for (DiscountRequest discountUpdateRequest : discountUpdateRequests) {
                Discount discount = discountRepo.findByCouponId(id).getFirst();

                discount.setType(discountUpdateRequest.getType());
                // Set category and product if applicable
                if (discountUpdateRequest.getCategoryId() != null) {
                    Category category = categoryRepository.findById(discountUpdateRequest.getCategoryId()).orElse(null);
                    discount.setCategory(category);
                }
                if (discountUpdateRequest.getProductId() != null) {
                    Product product = productRepository.findById(discountUpdateRequest.getProductId()).orElse(null);
                    discount.setProduct(product);
                }
                discount.setValueType(discountUpdateRequest.getValueType());
                discount.setValueFixed(discountUpdateRequest.getValueFixed());

                discountRepo.save(discount);
            }

            coupon.setLimitAccountUses(request.getLimitAccountUses());
            coupon.setLimitUses(request.getLimitUses());
            coupon.setDateUpdated(new Date());
            couponRepository.save(coupon);
        } else {
            throw new AppException("Coupon not found with id " + id, HttpStatus.BAD_REQUEST);
        }
    }
}
