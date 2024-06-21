package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class CouponResponse {
    private Long id;
    private String name;
    private String status;
    private String code;
    private Date dateCreated;
    private Date dateExpired;
    private Date dateUpdated;
    private Date dateValid;
    private String description;
    private DiscountResponse discount;
    private int limitAccountUses;
    private int limitUses;
    private int useCount;
}
