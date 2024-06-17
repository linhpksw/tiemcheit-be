package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.ProductIngredientRequest;
import com.tiemcheit.tiemcheitbe.model.ProductIngredient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface ProductIngredientMapper {
    //to ProductIngredient
    ProductIngredient toProductIngredient(ProductIngredientRequest request);
}
