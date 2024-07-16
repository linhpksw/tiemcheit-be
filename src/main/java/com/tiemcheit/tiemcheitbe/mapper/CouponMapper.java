package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.CouponRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CouponResponse;
import com.tiemcheit.tiemcheitbe.model.Coupon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    Coupon toEntity(CouponRequest request);

    @Mappings({
            @Mapping(target = "dateCreated", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
            @Mapping(target = "dateExpired", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
            @Mapping(target = "dateUpdated", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
            @Mapping(target = "dateValid", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    })
    CouponResponse toResponse(Coupon coupon);

    List<CouponResponse> toResponses(List<Coupon> coupons);
}
