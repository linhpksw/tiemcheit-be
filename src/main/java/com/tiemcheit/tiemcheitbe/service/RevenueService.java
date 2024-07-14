package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.model.Order;
import com.tiemcheit.tiemcheitbe.model.OrderDetail;
import com.tiemcheit.tiemcheitbe.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final OrderRepo orderRepo;

    @PreAuthorize("hasRole('ADMIN')")
    public Double getRevenue() {
        String status = "DELIVERED";
        List<Order> orderList = orderRepo.findAllByOrderStatus(status);
        List<List<OrderDetail>> orderDetailsList = orderList.stream().map(Order::getOrderDetails).toList();
        return orderDetailsList.stream().mapToDouble(orderDetails -> orderDetails.stream().mapToDouble(orderDetail -> orderDetail.getProduct().getPrice() * orderDetail.getQuantity()).sum()).sum();
    }


}
