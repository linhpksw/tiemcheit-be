package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "coupon", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String status;

    @Column(nullable = false, unique = true)
    private String code;

    private Date dateCreated;
    private Date dateExpired;
    private Date dateUpdated;
    private Date dateValid;
    private String description;
    @OneToOne(mappedBy = "coupon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Discount discount;
    private int limitAccountUses;
    private int limitUses;
    private int useCount;
}
