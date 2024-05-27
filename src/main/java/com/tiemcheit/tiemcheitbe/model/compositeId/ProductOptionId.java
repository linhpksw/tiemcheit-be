package com.tiemcheit.tiemcheitbe.model.compositeId;

import com.tiemcheit.tiemcheitbe.model.Option;
import com.tiemcheit.tiemcheitbe.model.Product;
import jakarta.persistence.Id;

import java.io.Serializable;

public class ProductOptionId implements Serializable {
    @Id
    private Product product;
    @Id
    private Option option;
}
