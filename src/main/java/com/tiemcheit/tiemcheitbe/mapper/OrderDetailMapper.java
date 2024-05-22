package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.OrderDetailDto;
import com.tiemcheit.tiemcheitbe.model.OrderDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface OrderDetailMapper {

    OrderDetail toEntity(OrderDetailDto orderDetailDto);

    OrderDetailDto toDto(OrderDetail orderDetail);
}
