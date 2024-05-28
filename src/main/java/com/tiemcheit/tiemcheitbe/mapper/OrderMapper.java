package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.OrderRequest;
import com.tiemcheit.tiemcheitbe.dto.response.OrderResponse;
import com.tiemcheit.tiemcheitbe.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = OrderDetailMapper.class)
public interface OrderMapper {

    Order toEntity(OrderRequest request);

    OrderResponse toReponse(Order order);

    List<OrderResponse> toResponses(List<Order> orders);
}
