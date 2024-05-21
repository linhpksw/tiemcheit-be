package com.tiemcheit.tiemcheitbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, name = "order_date")
    private Date orderDate;

    @Column(nullable = false, name = "shipping_address", length = 256)
    private String shippingAddress;

    // test
    @Column(nullable = false, name = "shipping_method", length = 256)
    private String shippingMethod;
    // test
    @Column(nullable = false, name = "payment_method", length = 256)
    private String paymentMethod;
    // test
    @Column(nullable = false, name = "order_status", length = 256)
    private String orderStatus;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderDetail> orderItems;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


}