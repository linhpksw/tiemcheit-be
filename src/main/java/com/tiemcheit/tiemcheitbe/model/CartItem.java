package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "ci_user_fk"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "ci_prod_fk"))
    private Product product;

    @Column(nullable = false)
    private int quantity;

}
