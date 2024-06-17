package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class OrderRequest {
    private Date orderDate;
    private String shippingAddress;
    private String shippingMethod;
    private String paymentMethod;
    private String message;
}
