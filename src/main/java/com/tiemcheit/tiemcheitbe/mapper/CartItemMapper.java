package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.CartItemDto;
import com.tiemcheit.tiemcheitbe.model.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface CartItemMapper {

    CartItem toCartItem(CartItemDto cartItemDto);

    CartItemDto toCartItemDto(CartItem cartItem);

    List<CartItemDto> toCartItemDtos(List<CartItem> cartItems);

}
