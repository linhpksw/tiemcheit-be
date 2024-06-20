package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CouponResponse {
    private String name;
    private boolean active;
    private String code;
    private Date dateCreated;
    private Date dateExpired;
    private Date dateUpdated;
    private Date dateValid;
    private String description;
    private List<DiscountResponse> discounts;
    private int limitAccountUses;
    private int limitUses;
    private int useCount;
}