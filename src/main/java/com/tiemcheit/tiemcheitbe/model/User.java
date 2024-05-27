package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<CartItem> cartItems;
    //    @Column(nullable = false, length = 256)
//    private String password;
//
    @Column(nullable = false, length = 10, unique = true)
    private String phone;
    //
    @Column(nullable = false, length = 50)
    private String email;
//
//    @Column(nullable = false, length = 10)
//    private String status;
//
//    @Column(nullable = false, length = 1)
//    private boolean isActivated;
//
//    @Column(nullable = false, length = 50)
//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<UserAddress> addresses;

}
