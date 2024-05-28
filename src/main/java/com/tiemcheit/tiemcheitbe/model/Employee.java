package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "employees")
@PrimaryKeyJoinColumn(name = "user_id")
public class Employee extends User {
    @OneToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    
}
