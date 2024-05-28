package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.response.OptionResponse;
import com.tiemcheit.tiemcheitbe.model.Option;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = OptionValueMapper.class)
public interface OptionMapper {
    OptionMapper INSTANCE = Mappers.getMapper(OptionMapper.class);

    //to OptionResponse
    OptionResponse toOptionResponse(Option option);

}
