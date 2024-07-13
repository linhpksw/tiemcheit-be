package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.OptionValueRequest;
import com.tiemcheit.tiemcheitbe.dto.response.OptionValueResponse;
import com.tiemcheit.tiemcheitbe.model.OptionValue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OptionValueMapper {
    //OptionValueMapper INSTANCE = Mappers.getMapper(OptionValueMapper.class);

    //to OptionValueResponse
    OptionValueResponse toOptionValueResponse(OptionValue optionValue);

    //to OptionValue
    OptionValue toOptionValue(OptionValueRequest optionValueRequest);
}
