package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

// just for test
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private @NotNull String name;
    private @NotNull String imageURL;
    private @NotNull double price;
    private @NotNull String description;

}