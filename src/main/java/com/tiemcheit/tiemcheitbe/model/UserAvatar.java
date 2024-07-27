package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_avatars")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAvatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String image;

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private User user;
}
