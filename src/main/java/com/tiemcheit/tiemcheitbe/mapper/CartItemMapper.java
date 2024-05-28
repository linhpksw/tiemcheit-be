package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.CartItemRequest;
import com.tiemcheit.tiemcheitbe.dto.request.CartItemUpdateRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.model.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface CartItemMapper {

    CartItem toEntity(CartItemRequest cartItemRequest);

    CartItem toEntity(CartItemUpdateRequest cartItemUpdateRequest);

    CartItemRequest toCartItemRequest(CartItem cartItem);

    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse> toCartItemResponses(List<CartItem> cartItems);

}
