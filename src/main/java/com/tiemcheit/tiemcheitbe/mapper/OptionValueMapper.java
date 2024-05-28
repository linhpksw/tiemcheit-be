package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.response.OptionValueResponse;
import com.tiemcheit.tiemcheitbe.model.OptionValue;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OptionValueMapper {
    //OptionValueMapper INSTANCE = Mappers.getMapper(OptionValueMapper.class);

    //to OptionValueResponse
    OptionValueResponse toOptionValueResponse(OptionValue optionValue);
}
