package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "coupon", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String status;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) COLLATE utf8_bin")
    private String code;

    private Date dateCreated;
    private Date dateExpired;
    private Date dateUpdated;
    private Date dateValid;
    private String description;
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Discount> discounts;
    private int limitAccountUses;
    private int limitUses;
    private int useCount;
}
