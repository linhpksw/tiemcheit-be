package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.response.LogResponse;
import com.tiemcheit.tiemcheitbe.model.Log;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LogMapper {
    LogResponse toLogResponse(Log log);
}
