package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "user_addresses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 256)
    private String address;

    @Column(nullable = false)
    private Boolean isDefault;
}
