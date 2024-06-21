package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.CouponRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.CouponResponse;
import com.tiemcheit.tiemcheitbe.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public ApiResponse<List<CouponResponse>> getAllCoupons() {
        return ApiResponse.<List<CouponResponse>>builder()
                .data(couponService.getAllCoupon())
                .message("Success")
                .build();
    }

    @PostMapping
    public ApiResponse<CouponResponse> createCoupon(@RequestBody CouponRequest coupon) {

        return ApiResponse.<CouponResponse>builder()
                .data(couponService.createCoupon(coupon))
                .message("Success")
                .build();
    }

    @PutMapping("/activate")
    public ApiResponse<Void> activateCoupons(@RequestBody List<Long> couponIds) {
        couponService.activateCoupons(couponIds);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PutMapping("/disable")
    public ApiResponse<Void> disableCoupons(@RequestBody Long couponId) {
        couponService.disableCoupons(couponId);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @DeleteMapping("/{couponId}")
    public ApiResponse<Void> deleteCoupon(@PathVariable Long couponId) {

        couponService.deleteCoupon(couponId);
        return ApiResponse.<Void>builder().message("Delete success").build();

    }
}
