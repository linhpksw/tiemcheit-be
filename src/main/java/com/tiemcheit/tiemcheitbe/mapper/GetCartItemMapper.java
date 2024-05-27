package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.GetCartItemDto;
import com.tiemcheit.tiemcheitbe.model.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface GetCartItemMapper {

    CartItem toEntity(GetCartItemDto cartItemDto);

    GetCartItemDto toDto(CartItem cartItem);

    List<GetCartItemDto> toDtos(List<CartItem> cartItems);

}
