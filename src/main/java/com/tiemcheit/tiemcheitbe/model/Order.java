package com.tiemcheit.tiemcheitbe.model;

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
    @Column(nullable = false, name = "shipping_method", length = 30)
    private String shippingMethod;
    // test
    @Column(nullable = false, name = "payment_method", length = 20)
    private String paymentMethod;
    // test
    @Column(nullable = false, name = "order_status", length = 20)
    private String orderStatus;

    @Column(nullable = true, name = "message", length = 256)
    private String message;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "order_user_fk"))
    private User user;


}