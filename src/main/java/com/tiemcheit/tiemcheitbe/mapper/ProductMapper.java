package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.response.CartProductResponse;
import com.tiemcheit.tiemcheitbe.model.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    CartProductResponse toCartProductResponse(Product product);

    Product toEntity(CartProductResponse productDto);
}
