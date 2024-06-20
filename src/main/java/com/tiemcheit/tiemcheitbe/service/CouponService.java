package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CouponRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CouponResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.CouponMapper;
import com.tiemcheit.tiemcheitbe.model.Category;
import com.tiemcheit.tiemcheitbe.model.Coupon;
import com.tiemcheit.tiemcheitbe.model.Discount;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.repository.CategoryRepo;
import com.tiemcheit.tiemcheitbe.repository.CouponRepo;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponMapper couponMapper;

    private final CouponRepo couponRepository;

    private final CategoryRepo categoryRepository;

    private final ProductRepo productRepository;

    @Transactional
    public List<CouponResponse> getAllCoupon() {
        return couponMapper.toResponses(couponRepository.findAllCoupon());
    }

    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        Coupon coupon = new Coupon();
        coupon.setCode(request.getCode());
        coupon.setName(request.getName());
        coupon.setDateExpired(request.getDateExpired());
        coupon.setDateValid(request.getDateValid());
        coupon.setDescription(request.getDescription());
        coupon.setLimitAccountUses(request.getLimitAccountUses());
        coupon.setLimitUses(request.getLimitUses());
        coupon.setActive(false); // Set other required fields
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
        return couponMapper.toResponse(couponRepository.save(coupon));
    }

    @Transactional
    public void activateCoupons(List<Long> couponIds) {
        List<Coupon> coupons = couponRepository.findAllById(couponIds);
        for (Coupon coupon : coupons) {
            coupon.setActive(true);
        }
        couponRepository.saveAll(coupons);
    }

    public double applyCouponToCart(String code, List<Product> products) {
        Coupon coupon = couponRepository.findByCode(code);
        String discountType = coupon.getDiscounts().getFirst().getType();
        double totalCost = 0;
        if (coupon == null) {
            throw new AppException("Coupon not found", HttpStatus.BAD_REQUEST);
        }
        if (!isCouponValid(coupon)) {
            throw new AppException("Coupon is not valid anymore", HttpStatus.BAD_REQUEST);
        }

        double totalDiscountAmount = 0.0;


        for (Product product : products) {
            if (!discountType.equals("total")) {
                totalDiscountAmount += applyProductDiscount(coupon.getDiscounts(), product);
            }
            totalCost += product.getPrice();
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

    private boolean isCouponValid(Coupon coupon) {
        // Implement validation logic here (e.g., check expiration date, usage limits)
        return true; // Placeholder
    }

    private double applyProductDiscount(List<Discount> discounts, Product product) {
        double discountAmount = 0.0;
        boolean canApply;

        for (Discount discount : discounts) {
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
        }

        return discountAmount;
    }

}
