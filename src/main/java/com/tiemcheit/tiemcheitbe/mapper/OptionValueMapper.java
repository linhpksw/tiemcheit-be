package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.response.OptionValueResponse;
import com.tiemcheit.tiemcheitbe.model.OptionValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OptionValueMapper {
    //OptionValueMapper INSTANCE = Mappers.getMapper(OptionValueMapper.class);

    //to OptionValueResponse
    @Mapping(target = "id", source = "value_id")
    @Mapping(target = "name", source = "value_name")
    OptionValueResponse toOptionValueResponse(OptionValue optionValue);
}
