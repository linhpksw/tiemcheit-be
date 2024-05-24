package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    private String description;

    private double price;

    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
