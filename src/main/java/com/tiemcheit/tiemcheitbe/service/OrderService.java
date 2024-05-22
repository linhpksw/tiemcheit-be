package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.OrderDto;
import com.tiemcheit.tiemcheitbe.mapper.OrderMapper;
import com.tiemcheit.tiemcheitbe.model.User;
import com.tiemcheit.tiemcheitbe.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;

    private final OrderMapper orderMapper;

    public List<OrderDto> listOrders(User user) {
        return orderMapper.toDtos(orderRepo.findAllByUserOrderByIdDesc(user));
    }

    // check the not found exception after
    public OrderDto getOrder(Long id) {
        return orderMapper.toDto(orderRepo.findById(id).get());
    }

    public void placeOrder(User user) {
        // first get the item from user's cart

        // create order and save it

        // add order item

        // delete item from cart
    }
}




