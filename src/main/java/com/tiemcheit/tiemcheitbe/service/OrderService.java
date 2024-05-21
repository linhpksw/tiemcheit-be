package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    OrderDetailService orderDetailService;

    public int saveOrder
}
