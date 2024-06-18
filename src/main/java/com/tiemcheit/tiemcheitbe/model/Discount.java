package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coupon_id", foreignKey = @ForeignKey(name = "cp_dis_fk"))
    private Coupon coupon;

    private String type; // category, product, total, ship

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    private String valueType; // percent, fixed
    private Double valueFixed;

}

