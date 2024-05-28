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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 256)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, length = 256)
    private String image;

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;

}