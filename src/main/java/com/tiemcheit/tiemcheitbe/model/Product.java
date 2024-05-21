package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 256)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 256)
    private String image;
    
    @Column(nullable = false)
    private int categoryId;
}