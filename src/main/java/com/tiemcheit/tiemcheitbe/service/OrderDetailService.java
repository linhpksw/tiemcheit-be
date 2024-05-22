package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.model.OrderDetail;
import com.tiemcheit.tiemcheitbe.repository.OrderDetailRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepo orderDetailRepo;

    public void addOrderedProduct(OrderDetail orderDetail) {
        orderDetailRepo.save(orderDetail);
    }
}
