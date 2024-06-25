package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.DiscountRequest;
import com.tiemcheit.tiemcheitbe.dto.response.DiscountResponse;
import com.tiemcheit.tiemcheitbe.model.Discount;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    Discount toEntity(DiscountRequest request);

    DiscountResponse toResponse(Discount discount);

    List<DiscountResponse> toResponses(List<Discount> discounts);
}
