package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.OrderDetailRequest;
import com.tiemcheit.tiemcheitbe.dto.response.OrderDetailResponse;
import com.tiemcheit.tiemcheitbe.model.OrderDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface OrderDetailMapper {

    OrderDetail toEntity(OrderDetailRequest request);

    OrderDetailResponse toResponse(OrderDetail orderDetail);
}
