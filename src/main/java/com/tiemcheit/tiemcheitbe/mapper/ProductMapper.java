package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.ProductDto;
import com.tiemcheit.tiemcheitbe.model.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);
}
