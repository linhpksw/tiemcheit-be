package com.tiemcheit.tiemcheitbe.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

    @Column(nullable = false, length = 256)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, length = 256)
    private String image;

    @Column(nullable = false)
    private LocalDate createAt = LocalDate.now();

    @Column(nullable = false)
    private Integer quantity;

    private Integer sold = 0;

    private String status;

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "product")
    private List<WishlistItem> wishlistItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<ProductOption> productOptions;

    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<ProductIngredient> productIngredients;
}
