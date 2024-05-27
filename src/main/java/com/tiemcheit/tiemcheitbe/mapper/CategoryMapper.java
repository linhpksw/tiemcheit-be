package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.CategoryRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CategoryResponse;
import com.tiemcheit.tiemcheitbe.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toCategory(CategoryRequest request);

    CategoryResponse toCategoryResponse(Category category);


}
