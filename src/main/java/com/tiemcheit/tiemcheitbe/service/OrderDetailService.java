package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.model.OrderDetail;
import com.tiemcheit.tiemcheitbe.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;


    public void addOrderedProduct(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }
}
