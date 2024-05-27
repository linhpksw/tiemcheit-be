package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.AddCartItemDto;
import com.tiemcheit.tiemcheitbe.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface AddCartItemMapper {

    CartItem toEntity(AddCartItemDto cartItemDto);

    AddCartItemDto toDto(CartItem cartItem);

}
