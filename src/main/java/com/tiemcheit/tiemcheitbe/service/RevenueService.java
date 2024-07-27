package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.model.Order;
import com.tiemcheit.tiemcheitbe.model.OrderDetail;
import com.tiemcheit.tiemcheitbe.repository.OrderDetailRepo;
import com.tiemcheit.tiemcheitbe.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final OrderRepo orderRepo;
    private final OrderDetailRepo orderDetailRepo;

    @PreAuthorize("hasRole('ADMIN')")
    public Double getRevenue() {
        String status = "Order Canceled";
        List<Order> orderList = orderRepo.findAllByOrderStatusExcept(status);
        List<List<OrderDetail>> orderDetailsList = orderList.stream().map(Order::getOrderDetails).toList();
        return orderDetailsList.stream().mapToDouble(orderDetails -> orderDetails.stream().mapToDouble(orderDetail -> orderDetail.getProduct().getPrice() * orderDetail.getQuantity()).sum()).sum();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Double[] sumOrderPricesByMonth(int year) {
        List<Object[]> results = orderRepo.sumOrderRevenueByMonth(year);
        Double[] totalRevenues = new Double[12];
        for (int i = 0; i < totalRevenues.length; i++) {
            totalRevenues[i] = 0.0;
        }
        for (Object[] result : results) {
            Integer month = (Integer) result[0] - 1;
            totalRevenues[month] = (Double) result[1];
        }
        return totalRevenues;
    }

}
