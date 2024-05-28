package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.response.OrderResponse;
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

    public List<OrderResponse> listOrders(User user) {
        return orderMapper.toResponses(orderRepo.findAllByUserOrderByIdDesc(user));
    }

    // check the not found exception after
    public OrderResponse getOrder(Long id) {
        return orderMapper.toReponse(orderRepo.findById(id).get());
    }

    public void placeOrder(User user) {
        // first get the item from user's cart

        // create order and save it

        // add order item

        // delete item from cart
    }
}




