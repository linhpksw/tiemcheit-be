package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.CouponRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CouponResponse;
import com.tiemcheit.tiemcheitbe.model.Coupon;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    Coupon toEntity(CouponRequest request);

    CouponResponse toResponse(Coupon coupon);

    List<CouponResponse> toResponses(List<Coupon> coupons);
}
