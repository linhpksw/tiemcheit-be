package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256)
    private String image;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
