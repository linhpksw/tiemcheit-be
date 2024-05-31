package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 256)
    private String password;

    @Column(length = 50, nullable = false)
    private String fullname;

    @Column(length = 10, unique = true, nullable = false)
    private String phone;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Builder.Default
    @Column(length = 50)
    private Date dob = null;

    @Builder.Default
    @Column(length = 10)
    private String status = "ACTIVE";

    @Builder.Default
    @Column(length = 1)
    private boolean isActivated = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",  // Explicitly defining the join table
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserAddress> addresses;
}
