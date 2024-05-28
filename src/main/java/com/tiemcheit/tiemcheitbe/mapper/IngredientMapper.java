package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.response.IngredientResponse;
import com.tiemcheit.tiemcheitbe.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IngredientMapper {
    IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);

    IngredientResponse toIngredientResponse(Ingredient ingredient);
}
