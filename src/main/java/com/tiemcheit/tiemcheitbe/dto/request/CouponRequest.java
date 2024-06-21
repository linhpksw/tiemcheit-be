package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class CouponRequest {
    private String code;
    private String name;
    private Date dateExpired;
    private Date dateValid;
    private String description;
    private DiscountRequest discount; // Use DiscountRequest instead of Discount
    private int limitAccountUses;
    private int limitUses;
}
