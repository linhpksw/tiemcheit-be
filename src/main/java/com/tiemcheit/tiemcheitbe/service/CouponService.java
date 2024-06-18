package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CouponRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CouponResponse;
import com.tiemcheit.tiemcheitbe.mapper.CouponMapper;
import com.tiemcheit.tiemcheitbe.model.Category;
import com.tiemcheit.tiemcheitbe.model.Coupon;
import com.tiemcheit.tiemcheitbe.model.Discount;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.repository.CategoryRepo;
import com.tiemcheit.tiemcheitbe.repository.CouponRepo;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
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
}
