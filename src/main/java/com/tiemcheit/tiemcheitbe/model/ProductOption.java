package com.tiemcheit.tiemcheitbe.model;

import com.tiemcheit.tiemcheitbe.model.compositeId.ProductOptionId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_options")
@IdClass(ProductOptionId.class)
public class ProductOption {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;
}
