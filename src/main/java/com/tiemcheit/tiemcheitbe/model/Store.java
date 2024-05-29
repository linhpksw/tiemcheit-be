package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 256)
    private String address;

    @OneToOne(mappedBy = "store", cascade = CascadeType.ALL)
    private Employee employee;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ingredient> ingredients;
}
