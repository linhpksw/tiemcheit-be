package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;

// just for test
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "image", nullable = false)
    private String image;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "description", nullable = false)
    private String description;

}